package com.hallym.rehab.domain.admin.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.hallym.rehab.domain.admin.dto.UploadFileDTO;
import com.hallym.rehab.domain.admin.dto.VideoRequestDTO;
import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.admin.repository.AdminRepository;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.repository.VideoRepository;
import com.hallym.rehab.global.config.S3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;
    private final AdminRepository adminRepository;
    private final VideoRepository videoRepository;

    @Override
    public String createVideo(VideoRequestDTO videoRequestDTO) {
        Admin admin = adminRepository.findById(videoRequestDTO.getAdmin_id())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));

        MultipartFile[] files = videoRequestDTO.getFiles();
        MultipartFile videoFile =  files[0];
        MultipartFile jsonFile =  files[1];
        UploadFileDTO uploadFileDTO = uploadFileToS3(videoFile, jsonFile);

        Video video = videoRequestDTO.toVideo(admin, uploadFileDTO);
        videoRepository.save(video);

        return "Success create Video";
    }

    @Override
    public String deleteVideo(Long vno) {
        Video video = videoRepository.findById(vno)
                .orElseThrow(() -> new RuntimeException("Video not found for Id : " + vno));

        String videoPath = video.getVideoPath();
        String jsonPath = video.getJsonPath();

        deleteFileFromS3(videoPath, jsonPath);
        videoRepository.delete(video);

        return "Success delete Video";
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
    public UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        UUID uuid_1 = UUID.randomUUID();
        UUID uuid_2 = UUID.randomUUID();
        String videoFileName = uuid_1 + "_" + videoFile.getOriginalFilename();
        String jsonFileName = uuid_2 + "_" + jsonFile.getOriginalFilename();

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
            String videoURL = baseUploadURL + guideVideoObjectPath;
            String jsonURL = baseUploadURL + jsonObjectPath;

            log.info(videoURL);
            log.info(jsonURL);

            setAcl(s3, guideVideoObjectPath);
            setAcl(s3, jsonObjectPath);

            return UploadFileDTO.builder()
                    .videoURL(videoURL)
                    .jsonURL(jsonURL)
                    .videoPath(guideVideoObjectPath)
                    .jsonPath(jsonObjectPath)
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
    public void deleteFileFromS3(String videoPath, String jsonPath) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        try {
            s3.deleteObject(bucketName, videoPath);
            s3.deleteObject(bucketName, jsonPath);
            log.info("Delete Object successfully");
        } catch(SdkClientException e) {
            e.printStackTrace();
            log.info("Error deleteFileFromS3");
        }
    }

    @Override
    public void setAcl(AmazonS3 s3, String objectPath) {
        AccessControlList objectAcl = s3.getObjectAcl(bucketName, objectPath);
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucketName, objectPath, objectAcl);
    }

    @Override
    public void clearAllVideoAndJson() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        ObjectListing videoObjectList = s3.listObjects(bucketName, "video/");
        while (true) {
            for (S3ObjectSummary summary : videoObjectList.getObjectSummaries()) {
                if (!summary.getKey().equals("video/")) { // Exclude the folder itself
                    s3.deleteObject(bucketName, summary.getKey());
                }
            }
            if (!videoObjectList.isTruncated()) break;
            videoObjectList = s3.listNextBatchOfObjects(videoObjectList);
        }

        ObjectListing jsonObjectList = s3.listObjects(bucketName, "json/");
        while (true) {
            for (S3ObjectSummary summary : jsonObjectList.getObjectSummaries()) {
                if (!summary.getKey().equals("json/")) { // Exclude the folder itself
                    s3.deleteObject(bucketName, summary.getKey());
                }
            }
            if (!jsonObjectList.isTruncated()) break;
            jsonObjectList = s3.listNextBatchOfObjects(jsonObjectList);
        }

        videoRepository.deleteAll();
    }
}
