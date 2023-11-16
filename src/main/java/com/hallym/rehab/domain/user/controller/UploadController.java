package com.hallym.rehab.domain.user.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.hallym.rehab.domain.user.dto.upload.UploadFileDTO;
import com.hallym.rehab.domain.user.dto.upload.UploadResultDTO;
import com.hallym.rehab.global.config.S3Client;
import com.hallym.rehab.global.exception.IsNotImageFileException;
import java.io.FileOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UploadController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;

    //post 방식으로 파일 등록
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResultDTO upload(UploadFileDTO uploadFileDTO) throws IOException {

        AmazonS3 s3 = s3Client.getAmazonS3();

        MultipartFile multipartFile = uploadFileDTO.getFile();

        String profileURL = null;

        if (multipartFile != null) {
            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString();

            // "_" 제거
            originalName = originalName.replaceAll("_", "");

            String originalFileName = uuid + "_" + originalName;
            String thumbFileName = "s_" + originalFileName;

            File originalFile = null;
            File thumbFile = new File(thumbFileName);

            try {
                originalFile = convertMultipartFileToFile(uploadFileDTO.getFile(), originalFileName);
                Thumbnailator.createThumbnail(originalFile, thumbFile, 400, 400);

                String objectPath = "profile/" + thumbFileName;

                String baseUploadURL = "https://kr.object.ncloudstorage.com/" + bucketName + "/";
                profileURL = baseUploadURL + objectPath;
                log.info(profileURL);

                s3.putObject(bucketName, objectPath, thumbFile);
                setAcl(s3, objectPath);


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert originalFile != null;
                assert thumbFile != null;
                originalFile.delete();
                thumbFile.delete();
            }

            return UploadResultDTO.builder()
                    .profileUrl(profileURL)
                    .build();

        } else {
            return null;
        }
    }

    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) {
        File convertedFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

    public void setAcl(AmazonS3 s3, String objectPath) {
        AccessControlList objectAcl = s3.getObjectAcl(bucketName, objectPath);
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucketName, objectPath, objectAcl);
    }
}
