package com.hallym.rehab.domain.room.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.hallym.rehab.domain.room.domain.Audio;
import com.hallym.rehab.domain.room.domain.Room;
import com.hallym.rehab.domain.room.dto.AudioRequestDTO;
import com.hallym.rehab.domain.room.dto.UploadAudioDTO;
import com.hallym.rehab.domain.room.repository.AudioRepository;
import com.hallym.rehab.domain.room.repository.RoomRepository;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AudioServiceImpl implements AudioService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;
    private final RoomRepository roomRepository;
    private final AudioRepository audioRepository;

    @Override
    public String registerAudio(AudioRequestDTO audioRequestDTO) {
        UUID rno = audioRequestDTO.getRno();
        boolean is_user = audioRequestDTO.is_user();
        MultipartFile audioFile = audioRequestDTO.getAudioFile();

        Optional<Room> roomOptional = roomRepository.findById(rno);
        if (audioFile.isEmpty()) return "Please select audioFile to upload";
        if (roomOptional.isEmpty()) return "Room not found for Id : " + rno;

        Room room = roomOptional.get();
        Optional<Audio> audioOptional = audioRepository.findByRoom(room);

        if (audioOptional.isEmpty()) { // 테이블이 없을 경우
            UploadAudioDTO uploadAudioDTO = uploadFileToS3(audioFile);

            if (is_user) {
                Audio audio = Audio.builder()
                        .userAudioURL(uploadAudioDTO.getAudioURL())
                        .userAudioObjectPath(uploadAudioDTO.getAudioObjectPath())
                        .room(room)
                        .build();
                audioRepository.save(audio);
            } else {
                Audio audio = Audio.builder()
                        .adminAudioURL(uploadAudioDTO.getAudioURL())
                        .adminAudioObjectPath(uploadAudioDTO.getAudioObjectPath())
                        .room(room)
                        .build();
                audioRepository.save(audio);
            }

        } else { // 이미 테이블이 있을경우
            Audio audio = audioOptional.get();
            UploadAudioDTO uploadAudioDTO = uploadFileToS3(audioFile);

            if (is_user) { //dirty checking update
                audio.setUserAudio(uploadAudioDTO.getAudioURL(), uploadAudioDTO.getAudioObjectPath());
                audioRepository.save(audio);
            } else {
                audio.setAdminAudio(uploadAudioDTO.getAudioURL(), uploadAudioDTO.getAudioObjectPath());
                audioRepository.save(audio);
            }
        }

        return "Success create Audio";
    }

    @Override
    public UploadAudioDTO uploadFileToS3(MultipartFile audioFile) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        UUID uuid = UUID.randomUUID();
        String audioFileName = uuid + "_" + audioFile.getOriginalFilename();
        File uploadAudioFile = null;

        try {
            uploadAudioFile = convertMultipartFileToFile(audioFile, audioFileName);
            String audioObjectPath = "audio/" + audioFileName;
            s3.putObject(bucketName, audioObjectPath, uploadAudioFile);
            String audioURL = "https://kr.object.ncloudstorage.com/" + bucketName + "/" + audioObjectPath;
            setAcl(s3, audioObjectPath);

            return UploadAudioDTO.builder()
                    .audioObjectPath(audioObjectPath)
                    .audioURL(audioURL).build();
        } catch (AmazonS3Exception e) {
            throw new RuntimeException(e.getErrorMessage());
        } finally {
            if (uploadAudioFile != null) uploadAudioFile.delete();
        }
    }

    @Override
    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) {
        File convertedFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
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
    public void deleteAllRoomAndAudio() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        ObjectListing objectListing = s3.listObjects(bucketName, "audio/");
        while (true) {
            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                if (!summary.getKey().equals("audio/")) { // Exclude the folder itself
                    s3.deleteObject(bucketName, summary.getKey());
                }
            }
            if (!objectListing.isTruncated()) break;
            objectListing = s3.listNextBatchOfObjects(objectListing);
        }


        audioRepository.deleteAll();
        roomRepository.deleteAll();
    }
}
