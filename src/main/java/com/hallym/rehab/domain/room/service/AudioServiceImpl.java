package com.hallym.rehab.domain.room.service;

import com.hallym.rehab.domain.room.entity.Audio;
import com.hallym.rehab.domain.room.entity.Room;
import com.hallym.rehab.domain.room.dto.AudioRequestDTO;
import com.hallym.rehab.domain.room.repository.AudioRepository;
import com.hallym.rehab.domain.room.repository.RoomRepository;
import com.hallym.rehab.global.s3.dto.UploadResponseDTO;
import com.hallym.rehab.global.s3.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {

    private final S3Util s3Util;
    private final RoomRepository roomRepository;
    private final AudioRepository audioRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 오디오"));
        UploadResponseDTO uploadResponseDTO = s3Util.uploadFileToS3(audioFile, "audio");

        if (is_user) { //dirty checking update
            audio.setUserAudio(uploadResponseDTO.getUrl(), uploadResponseDTO.getObjectPath());
            audioRepository.saveAndFlush(audio);
        } else {
            audio.setAdminAudio(uploadResponseDTO.getUrl(), uploadResponseDTO.getObjectPath());
            audioRepository.saveAndFlush(audio);
        }

        if (audio.getUserAudioURL() != null && audio.getAdminAudioURL() != null) {
            Long ano = audio.getAno();
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject("http://210.115.229.219:8000/getSummary?ano=" + ano.toString(),
                    String.class);
        }

        return "Success create Audio";
    }

//    @Override
//    public void deleteAllRoomAndAudio() {
//        AmazonS3 s3 = s3Client.getAmazonS3();
//
//        ObjectListing objectListing = s3.listObjects(bucketName, "audio/");
//        while (true) {
//            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
//                if (!summary.getKey().equals("audio/")) { // Exclude the folder itself
//                    s3.deleteObject(bucketName, summary.getKey());
//                }
//            }
//            if (!objectListing.isTruncated()) {
//                break;
//            }
//            objectListing = s3.listNextBatchOfObjects(objectListing);
//        }
//
//        audioRepository.deleteAll();
//        roomRepository.deleteAll();
//    }
}
