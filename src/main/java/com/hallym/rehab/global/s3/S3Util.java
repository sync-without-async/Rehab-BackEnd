package com.hallym.rehab.global.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.hallym.rehab.global.s3.dto.UploadVideoResponseDTO;
import com.hallym.rehab.global.s3.dto.UploadResponseDTO;
import com.hallym.rehab.global.util.AWTUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class S3Util {
    private final S3Client s3Client;

    /**
     * @param videoFile
     * @param jsonFile
     * @return
     */
    public UploadVideoResponseDTO uploadVideoAndJson(MultipartFile videoFile, MultipartFile jsonFile) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        UUID uuid_1 = UUID.randomUUID();
        UUID uuid_2 = UUID.randomUUID();
        UUID uuid_3 = UUID.randomUUID();

        String videoFileName = uuid_1 + "_" + videoFile.getOriginalFilename();
        String jsonFileName = uuid_2 + "_" + jsonFile.getOriginalFilename();
        String thumbnailFileName = uuid_3 + "_" + videoFile.getOriginalFilename().split("\\.")[0] + ".png";

        File uploadVideoFile = null;
        File uploadJsonFile = null;
        File uploadThumbnailFile = null;

        try {
            uploadVideoFile = convertMultipartFileToFile(videoFile, videoFileName);
            uploadJsonFile = convertMultipartFileToFile(jsonFile, jsonFileName);

            FileChannelWrapper channel = NIOUtils.readableChannel(uploadVideoFile);
            FrameGrab grab = FrameGrab.createFrameGrab(channel);

            // Get the first frame
            Picture picture = grab.getNativeFrame();
            // Convert the Picture object to a BufferedImage
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            // Now you can use the BufferedImage as needed. For example, write it to a file:
            uploadThumbnailFile = new File(thumbnailFileName);
            ImageIO.write(bufferedImage, "png", uploadThumbnailFile);

            String guideVideoObjectPath = "video/" + videoFileName;
            String jsonObjectPath = "json/" + jsonFileName;
            String thumbnailObjectPath = "thumbnail/" + thumbnailFileName;

            s3.putObject(s3Client.getBucketName(), guideVideoObjectPath, uploadVideoFile);
            s3.putObject(s3Client.getBucketName(), jsonObjectPath, uploadJsonFile);
            s3.putObject(s3Client.getBucketName(), thumbnailObjectPath, uploadThumbnailFile);

            String baseUploadURL = "https://kr.object.ncloudstorage.com/" + s3Client.getBucketName() + "/";
            String videoURL = baseUploadURL + guideVideoObjectPath;
            String jsonURL = baseUploadURL + jsonObjectPath;
            String thumbnailURL = baseUploadURL + thumbnailObjectPath;

            log.info(videoURL);
            log.info(jsonURL);
            log.info(thumbnailURL);

            setAcl(guideVideoObjectPath);
            setAcl(jsonObjectPath);
            setAcl(thumbnailObjectPath);

            // close readable channel to delete temp file
            channel.close();

            return UploadVideoResponseDTO.builder()
                    .videoURL(videoURL)
                    .jsonURL(jsonURL)
                    .thumbnailURL(thumbnailURL)
                    .videoPath(guideVideoObjectPath)
                    .jsonPath(jsonObjectPath)
                    .thumbnailPath(thumbnailObjectPath)
                    .build();

        } catch (AmazonS3Exception e) { // ACL Exception
            log.info(e.getErrorMessage());
            System.exit(1);
            return null; // if error during upload, return null
        } catch (JCodecException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Delete temporary files used when uploading
            assert uploadJsonFile != null;
            assert uploadVideoFile != null;
            assert uploadThumbnailFile != null;

            uploadVideoFile.delete();
            uploadJsonFile.delete();
            uploadThumbnailFile.delete();
        }
    }

    /**
     * 일반적인 파일 전용 업로더
     *
     * @param multipartFile
     * @return
     */
    public UploadResponseDTO uploadFileToS3(MultipartFile multipartFile, String uploadPath) {
        AmazonS3 s3 = s3Client.getAmazonS3();
        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        File file = convertMultipartFileToFile(multipartFile, fileName);
        String objectPath = uploadPath + "/" + fileName;
        String baseUploadUrl = "https://kr.object.ncloudstorage.com/" + s3Client.getBucketName() + "/";
        String url = baseUploadUrl + objectPath;

        try {
            s3.putObject(s3Client.getBucketName(), objectPath, file);
            setAcl(objectPath);
            log.info(url);

            return UploadResponseDTO.builder()
                    .url(url)
                    .objectPath(objectPath)
                    .build();

        } catch (AmazonS3Exception e) { // ACL Exception
            log.info(e.getErrorMessage());
            System.exit(1);
            return null; // if error during upload, return null
        } finally {
            // Delete temporary files used when uploading
            assert file != null;
            file.delete();
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

    public void deleteFileFromS3(String objectPath) {
        AmazonS3 s3 = s3Client.getAmazonS3();
        try {
            s3.deleteObject(s3Client.getBucketName(), objectPath);
            log.info("Delete Object successfully");
        } catch (SdkClientException e) {
            e.printStackTrace();
            log.info("Error deleteFileFromS3");
        }
    }

    public void setAcl(String objectPath) {
        AmazonS3 s3 = s3Client.getAmazonS3();
        AccessControlList objectAcl = s3.getObjectAcl(s3Client.getBucketName(), objectPath);
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(s3Client.getBucketName(), objectPath, objectAcl);
    }
}
