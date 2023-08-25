package com.hallym.rehab.domain.program.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.hallym.rehab.domain.program.dto.ProgramDTO;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramVideo;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.ProgramVideoRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import com.hallym.rehab.global.config.S3Client;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProgramServiceImpl implements ProgramService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;
    private final MemberRepository memberRepository;
    private final ProgramRepository programRepository;
    private final ProgramVideoRepository programVideoRepository;

    @Override
    public ProgramDTO getProgramOne(Long pno) {

        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        ProgramDTO programRequestDTO = entityToDTO(program);
        log.info("-------ProgramDTO: " + programRequestDTO);

        return programRequestDTO;
    }

    @Override
    public String modifyProgramOne(Long pno, ProgramRequestDTO programRequestDTO, MultipartFile videoFile, MultipartFile jsonFile) {

        log.info(pno);

        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        log.info(program + "------------------------------------------------------");

        program.modifyProgram(programRequestDTO.getProgramTitle(), programRequestDTO.getDescription(), programRequestDTO.getCategory(), programRequestDTO.getPosition());

        List<ProgramVideo> byProgram = programVideoRepository.findByProgram(program);

        byProgram.forEach(programVideo -> {
            deleteFileFromS3(programVideo.getGuideVideoObjectPath(), programVideo.getJsonObjectPath());
        });

        uploadFileToS3(videoFile, jsonFile, program);

        programRepository.save(program);

        return "Program modify successfully.";
    }

    @Override
    public String deleteProgramOne(Long pno) {

        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

//        List<ProgramVideo> byProgram = programVideoRepository.findByProgram(program);
//
//
//        byProgram.forEach(programVideo -> {
//            deleteFileFromS3(programVideo.getGuideVideoObjectPath(), programVideo.getJsonObjectPath());
//            programVideoRepository.delete(programVideo);
//        });


        program.setIs_deleted(Boolean.TRUE);
        programRepository.save(program);

        return "Program delete successfully.";
    }

    @Override
    public PageResponseDTO<ProgramDTO> getProgramList(PageRequestDTO pageRequestDTO) {

        return null;
    }

    @Override
    public void uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile, Program program) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        UUID uuid = UUID.randomUUID();
        String videoFileName = uuid + "_" + videoFile.getOriginalFilename();
        String jsonFileName = uuid + "_" + jsonFile.getOriginalFilename();

        File uploadVideoFile = null;
        File uploadJsonFile = null;

        try {
            uploadVideoFile = convertMultipartFileToFile(videoFile, videoFileName);
            uploadJsonFile = convertMultipartFileToFile(jsonFile, jsonFileName);

            String guideVideoObjectPath = "video/" + videoFileName;
            String jsonObjectPath = "json/" + jsonFileName;

            s3.putObject(bucketName, guideVideoObjectPath, uploadVideoFile);
            s3.putObject(bucketName, jsonObjectPath, uploadJsonFile);

            String baseUploadURL = "https://kr.object.ncloudstorage.com/rehab/";
            String guideVideoURL = baseUploadURL + guideVideoObjectPath;
            String jsonURL = baseUploadURL + jsonObjectPath;

            log.info(guideVideoURL);
            log.info(jsonURL);

            setAcl(s3, guideVideoObjectPath);
            setAcl(s3, jsonObjectPath);

            List<ProgramVideo> byProgram = programVideoRepository.findByProgram(program);

            if (byProgram.isEmpty()) {
                ProgramVideo programVideo = ProgramVideo.builder()
                        .GuideVideoURL(guideVideoURL)
                        .JsonURL(jsonURL)
                        .GuideVideoObjectPath(guideVideoObjectPath)
                        .JsonObjectPath(jsonObjectPath)
                        .program(program)
                        .build();
                programVideoRepository.save(programVideo);
            } else {
                ProgramVideo programVideo = byProgram.get(0);
                programVideo.changeProgramVideo(guideVideoURL, jsonURL, guideVideoObjectPath, jsonObjectPath);
                programVideoRepository.save(programVideo);
            }

//            program.addProgramVideo(programVideo);

        } catch (AmazonS3Exception e) { // ACL Exception
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } finally {
            // 업로드에 사용한 임시 파일을 삭제합니다.
            if (uploadVideoFile != null) uploadVideoFile.delete();
            if (uploadJsonFile != null) uploadJsonFile.delete();
        }
    }

    @Override
    public String createProgram(ProgramRequestDTO programRequestDTO,MultipartFile videoFile,MultipartFile jsonFile) {
        Program program = programRequestDtoToProgram(programRequestDTO);
        programRepository.save(program);
        uploadFileToS3(videoFile, jsonFile, program);
        return "Program create successfully.";
    }

    @Override
    public void deleteFileFromS3(String guideVideoObjectPath, String jsonObjectPath) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        try {
            s3.deleteObject(bucketName, guideVideoObjectPath);
            s3.deleteObject(bucketName, jsonObjectPath);
            log.info("Delete Object successfully");
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) {
        File convertedFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

    @Override
    public void setAcl(AmazonS3 s3, String objectPath) {
        AccessControlList objectAcl = s3.getObjectAcl(bucketName, objectPath);
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucketName, objectPath, objectAcl);
    }

    @Override
    public Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO) {
//        Optional<Member> byId = memberRepository.findById(programRequestDTO.getMid());
//        if (byId.isEmpty()) {
//            throw new NotFoundException("User is Not Found");
//        }

        Program program = dtoToEntity(programRequestDTO);

        return program;
    }
}
