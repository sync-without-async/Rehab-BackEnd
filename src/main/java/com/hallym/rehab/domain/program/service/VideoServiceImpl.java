package com.hallym.rehab.domain.program.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.hallym.rehab.domain.program.dto.upload.UploadFileDTO;
import com.hallym.rehab.domain.program.dto.video.MetricsRequestDTO;
import com.hallym.rehab.domain.program.dto.video.ChangeOrdRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoResponseDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.Video;
import com.hallym.rehab.domain.program.entity.Video_Member;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.VideoRepository;
import com.hallym.rehab.domain.program.repository.Video_MemberRepository;
import com.hallym.rehab.global.config.S3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        double playtime = videoRequestDTO.getPlayTime();
        Long frame = videoRequestDTO.getFrame();
        MultipartFile videoFile =  files[0];
        MultipartFile jsonFile =  files[1];

        if (videoFile.isEmpty() || jsonFile.isEmpty()) return "Please select files to upload";

        Optional<Video> byPno = videoRepository.findByPnoAndOrd(pno, ord);

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
                    .playTime(playtime)
                    .frame(frame)
                    .build();

        videoRepository.save(video);

        program.addVideo(video);

        log.info(program.getVideo().size());

        return "Success create Video";
    }

    @Override
    public String deleteVideo(Long pno, Long ord) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        Optional<Video> byPno = videoRepository.findByPnoAndOrd(pno, ord);
        if (byPno.isEmpty()) return "Please Select Valid Ord";

        Video video = byPno.get();
        String guideVideoObjectPath = video.getGuideVideoObjectPath();
        String jsonObjectPath = video.getJsonObjectPath();

        deleteFileFromS3(guideVideoObjectPath, jsonObjectPath);

        videoRepository.delete(video);
        program.deleteVideo(video);

        return "Success delete Video";
    }

    @Override
    public VideoResponseDTO getVideoList(Long pno) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        List<Video> videoList = videoRepository.findByPno(pno);

        return videoToVideoResponseDTO(videoList, program);
    }

    @Override
    public String saveMetrics(Long vno, MetricsRequestDTO metricsRequestDTO) {
        videoRepository.findById(vno)
                .orElseThrow(() -> new RuntimeException("Video not found for Id : " + vno));

        String mid = metricsRequestDTO.getMid();
        double metrics = metricsRequestDTO.getMetrics();

        Optional<Video_Member> byMemberAndVideo = videoMemberRepository.findByMemberAndVideo(mid, vno);
        if (byMemberAndVideo.isEmpty()) return "findByMemberAndVideo error";

        Video_Member videoMember = byMemberAndVideo.get();
        videoMember.changeMetrics(metrics);
        videoMemberRepository.save(videoMember);

        return "Metrics saved";
    }

    @Override
    public String changeVideoOrd(Long pno, ChangeOrdRequestDTO changeOrdRequestDTO) {
        programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        for (Map.Entry<Long, Long> entry : changeOrdRequestDTO.getOrd_map().entrySet()) {
            Long vno = entry.getKey();
            Long ord = entry.getValue();

            Video video = videoRepository.findById(vno)
                    .orElseThrow(() -> new RuntimeException("Video not found for Id : " + vno));
            video.setOrd(ord);
            videoRepository.save(video);
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

            String baseUploadURL = "https://kr.object.ncloudstorage.com/" + bucketName + "/";
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
