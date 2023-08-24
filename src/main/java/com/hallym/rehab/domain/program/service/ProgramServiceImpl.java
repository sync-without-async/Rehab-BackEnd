package com.hallym.rehab.domain.program.service;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProgramServiceImpl implements ProgramService{

    private final S3Client s3Client;
    private final MemberRepository memberRepository;
    private final ProgramRepository programRepository;
    private final ProgramVideoRepository programVideoRepository;

    @Override
    public ProgramDTO getProgramOne(Long bno) {

        Program program = programRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + bno));

        ProgramDTO programDTO = entityToDTO(program);
        log.info("-------ProgramDTO: " + programDTO);

        return programDTO;
    }

    @Override
    public String modifyProgramOne(Long bno, ProgramDTO programDTO) {

        Program program = programRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + bno));

        program.modifyProgram(programDTO.getProgramTitle(), programDTO.getDescription(), programDTO.getCategory(), programDTO.getPosition());

        program.clearProgramVideo();

        programRepository.save(program);

        return "Program modify successfully.";
    }

    @Override
    public String deleteProgramOne(Long bno) {

        Program program = programRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + bno));

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

            String bucketName = "rehab";
            String guideVideoObjectPath = "video/" + videoFileName;
            String jsonObjectPath = "json/" + jsonFileName;

            s3.putObject(bucketName, guideVideoObjectPath, uploadVideoFile);
            s3.putObject(bucketName, jsonObjectPath, uploadJsonFile);

            String guideVideoURL = "https://kr.object.ncloudstorage.com/rehab/" + guideVideoObjectPath;
            String jsonURL = "https://kr.object.ncloudstorage.com/rehab/" + jsonObjectPath;

            log.info(guideVideoURL);
            log.info(jsonURL);

            setAcl(s3, bucketName, guideVideoObjectPath);
            setAcl(s3, bucketName, jsonObjectPath);

            ProgramVideo programVideo = ProgramVideo.builder()
                    .GuideVideoURL(guideVideoURL)
                    .JsonURL(jsonURL)
                    .GuideVideoObjectPath(guideVideoObjectPath)
                    .JsonObjectPath(jsonObjectPath)
                    .program(program)
                    .build();

            programVideoRepository.save(programVideo);

        } catch (AmazonS3Exception e) { // ACL Exception
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } finally {
            // 업로드에 사용한 임시 파일을 삭제합니다.
            if (uploadVideoFile != null) {
                uploadVideoFile.delete();
            }
            if (uploadJsonFile != null) {
                uploadJsonFile.delete();
            }
        }
    }

    @Override
    public Program createProgram(ProgramRequestDTO programRequestDTO) {
        Program program = programRequestDtoToProgram(programRequestDTO);
        return programRepository.save(program);
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
    public void setAcl(AmazonS3 s3, String bucketName, String objectPath) {
        AccessControlList objectAcl = s3.getObjectAcl(bucketName, objectPath);
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucketName, objectPath, objectAcl);
    }

    @Override
    public Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO) {
        Optional<Member> byId = memberRepository.findById(programRequestDTO.getMid());
        if (byId.isEmpty()) {
            throw new NotFoundException("존재하지 않는 유저입니다.");
        }

        return Program.builder()
                .programTitle(programRequestDTO.getProgramTitle())
                .category(programRequestDTO.getCategory())
                .description(programRequestDTO.getDescription())
                .member(byId.get())
                .build();
    }
}
