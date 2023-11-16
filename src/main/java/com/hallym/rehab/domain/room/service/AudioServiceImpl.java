package com.hallym.rehab.domain.room.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.hallym.rehab.domain.room.entity.Audio;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.dto.AudioRequestDTO;
import com.hallym.rehab.domain.room.dto.UploadAudioDTO;
import com.hallym.rehab.domain.room.repository.AudioRepository;
import com.hallym.rehab.domain.room.repository.RoomRepository;
import com.hallym.rehab.global.config.S3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Client s3Client;
    private final RoomRepository roomRepository;
    private final AudioRepository audioRepository;
    private final ConcurrentHashMap<UUID, Integer> audio_check = new ConcurrentHashMap<>();

    @Override
    public String registerAudio(AudioRequestDTO audioRequestDTO) {
        UUID rno = audioRequestDTO.getRno();
        boolean is_user = audioRequestDTO.getIs_patient();
        MultipartFile audioFile = audioRequestDTO.getAudioFile();

        Optional<Room> roomOptional = roomRepository.findById(rno);
        if (audioFile.isEmpty()) {
            return "Please select audioFile to upload";
        }
        if (roomOptional.isEmpty()) {
            return "Room not found for Id : " + rno;
        }

        Room room = roomOptional.get();
        Audio audio = audioRepository.findByRoom(room)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방"));

        UploadAudioDTO uploadAudioDTO = uploadFileToS3(audioFile);

        if (is_user) { //dirty checking update
            audio.setUserAudio(uploadAudioDTO.getAudioURL(), uploadAudioDTO.getAudioObjectPath());
            audioRepository.saveAndFlush(audio);
        } else {
            audio.setAdminAudio(uploadAudioDTO.getAudioURL(), uploadAudioDTO.getAudioObjectPath());
            audioRepository.saveAndFlush(audio);
        }

        audio_check.putIfAbsent(rno, 0);
        Integer size = audio_check.get(rno);

        if (size.equals(0)) {
            audio_check.put(rno, 1);
        } else if (size.equals(1)) {
            audio_check.put(rno, 0); // 다시 0으로 초기화
            Long ano = audio.getAno();

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject("http://210.115.229.219:8000/getSummary?ano=" + ano.toString(),
                    String.class);
        }

        log.info("size : " + audio_check.get(rno));
        return "Success create Audio";
    }

    @Override
    public synchronized UploadAudioDTO uploadFileToS3(MultipartFile audioFile) {
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
            if (uploadAudioFile != null) {
                uploadAudioFile.delete();
            }
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
            if (!objectListing.isTruncated()) {
                break;
            }
            objectListing = s3.listNextBatchOfObjects(objectListing);
        }

        audioRepository.deleteAll();
        roomRepository.deleteAll();
    }
}
