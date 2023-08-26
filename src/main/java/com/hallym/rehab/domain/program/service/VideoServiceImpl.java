package com.hallym.rehab.domain.program.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.hallym.rehab.domain.program.dto.upload.UploadFileDTO;
import com.hallym.rehab.domain.program.dto.video.MatrixRequestDTO;
import com.hallym.rehab.domain.program.dto.video.SwapOrdRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.Video;
import com.hallym.rehab.domain.program.entity.Video_Member;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.VideoRepository;
import com.hallym.rehab.domain.program.repository.Video_MemberRepository;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import com.hallym.rehab.global.config.S3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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
public class VideoServiceImpl implements VideoService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;
    private final VideoRepository videoRepository;
    private final ProgramRepository programRepository;
    private final Video_MemberRepository videoMemberRepository;

    @Override // 프로그램 등록 후 영상 등록
    public String createVideo(Long pno, Long ord, VideoRequestDTO videoRequestDTO) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        MultipartFile[] files = videoRequestDTO.getFiles();
        String actName =  videoRequestDTO.getActName();
        MultipartFile videoFile =  files[0];
        MultipartFile jsonFile =  files[1];

        if (videoFile.isEmpty() || jsonFile.isEmpty()) return "Please select files to upload";

        Optional<Video> byPno = videoRepository.findByPno(pno, ord);

        if (byPno.isPresent()) return "already exists ord";

        UploadFileDTO uploadFileDTO = uploadFileToS3(videoFile, jsonFile, program);

        Video video = Video.builder()
                    .GuideVideoURL(uploadFileDTO.getGuideVideoURL())
                    .JsonURL(uploadFileDTO.getJsonURL())
                    .GuideVideoObjectPath(uploadFileDTO.getGuideVideoObjectPath())
                    .JsonObjectPath(uploadFileDTO.getJsonObjectPath())
                    .program(program)
                    .ord(ord)
                    .ActName(actName)
                    .build();

        videoRepository.save(video);

        program.addProgramVideo(video);

        log.info(program.getVideo().size());

        return "Success create Video";
    }

    @Override
    public String deleteVideo(Long pno, Long ord) {
        programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        Optional<Video> byPno = videoRepository.findByPno(pno, ord);
        if (byPno.isEmpty()) return "Please Select Valid Ord";

        Video video = byPno.get();
        String guideVideoObjectPath = video.getGuideVideoObjectPath();
        String jsonObjectPath = video.getJsonObjectPath();

        deleteFileFromS3(guideVideoObjectPath, jsonObjectPath);

        videoRepository.delete(video);
        return "Success delete Video";
    }

    @Override
    public String saveMatrix(Long vno, MatrixRequestDTO matrixRequestDTO) {
        videoRepository.findById(vno)
                .orElseThrow(() -> new RuntimeException("Video not found for Id : " + vno));

        String mid = matrixRequestDTO.getMid();
        double matrix = matrixRequestDTO.getMatrix();

        Optional<Video_Member> byMemberAndVideo = videoMemberRepository.findByMemberAndVideo(mid, vno);
        if (byMemberAndVideo.isEmpty()) return "findByMemberAndVideo error";

        Video_Member videoMember = byMemberAndVideo.get();
        videoMember.changeMatrix(matrix);
        videoMemberRepository.save(videoMember);

        return "Matrix saved";
    }

    @Override // 비디오의 순서만을 바꿈.
    public String swapVideoOrd(Long pno, SwapOrdRequestDTO swapOrdRequestDTO) {
        programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        Long ord1 = swapOrdRequestDTO.getOrd_1();
        Long ord2 = swapOrdRequestDTO.getOrd_2();


        Optional<Video> byPno_1 = videoRepository.findByPno(pno, ord1);
        Optional<Video> byPno_2 = videoRepository.findByPno(pno, ord2);

        // 만약 ord1과 ord2 의 video 객체가 둘다 없다면 에러처리
        if (byPno_1.isEmpty() && byPno_2.isEmpty()) return "Please Select Valid Ord";
        // 하나라도 있다면
        if (byPno_1.isPresent()) {
            Video pv1 = byPno_1.get();
            pv1.setOrd(ord2);
            videoRepository.save(pv1);
        } else {
            Video pv2 = byPno_2.get();
            pv2.setOrd(ord1);
            videoRepository.save(pv2);
        }

        return "Success modify Video Ord";
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
            log.info("Error deleteFileFromS3");
        }
    }

    @Override
    public UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile, Program program) {
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

            return UploadFileDTO.builder()
                    .guideVideoURL(guideVideoURL)
                    .jsonURL(jsonURL)
                    .guideVideoObjectPath(guideVideoObjectPath)
                    .jsonObjectPath(jsonObjectPath)
                    .build();

        } catch (AmazonS3Exception e) { // ACL Exception
            log.info(e.getErrorMessage());
            System.exit(1);
            return null; // 업로드 오류 시 null 반환
        } finally {
            // 업로드에 사용한 임시 파일을 삭제합니다.
            if (uploadVideoFile != null) uploadVideoFile.delete();
            if (uploadJsonFile != null) uploadJsonFile.delete();
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
}
