package com.hallym.rehab.global.config;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3ClientTest {

    @Autowired
    S3Client s3Client;

    private String baseUploadURL = "https://kr.object.ncloudstorage.com/rehab/";

    @Test
    public void 버킷목록조회() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        try {
            List<Bucket> buckets = s3.listBuckets();
            System.out.println("Bucket List: ");
            for (Bucket bucket : buckets) {
                System.out.println("    name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void 버킷에파일업로드() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        String bucketName = "rehab";
        // upload local file
        String objectName = "video/video1";
        String filePath = "src/main/resources/sampleVideo.mp4";

        try {
            s3.putObject(bucketName, objectName, new File(filePath));
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void 파일삭제() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        String bucketName = "rehab";
        String objectName = "video/video1";

        // delete object
        try {
            s3.deleteObject(bucketName, objectName);
            System.out.format("Object %s has been deleted.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

}