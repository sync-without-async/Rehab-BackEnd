package com.hallym.rehab.domain.user.controller;

import com.hallym.rehab.domain.user.dto.upload.UploadFileDTO;
import com.hallym.rehab.domain.user.dto.upload.UploadResultDTO;
import com.hallym.rehab.global.exception.IsNotImageFileException;
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
@Log4j2
public class UploadController {

    @Value("${com.hallym.rehab.upload.path}")
    private String uploadPath;

    //post 방식으로 파일 등록
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResultDTO upload(UploadFileDTO uploadFileDTO) throws IOException {

        log.info(uploadFileDTO.toString());

        MultipartFile multipartFile = uploadFileDTO.getFile();

        if(multipartFile != null){

            // 이미지 파일인지 확인
            try {
                ImageIO.read(multipartFile.getInputStream()).toString();
                // 이미지가 아닌 경우, Exception 발생
            } catch(Exception e) {
                throw new IsNotImageFileException(multipartFile.getOriginalFilename());
            }

            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString();

            // "_" 제거
            originalName = originalName.replaceAll("_", "");

            Path savePath = Paths.get(uploadPath, uuid+"_"+ originalName);

            try {
                multipartFile.transferTo(savePath);

                log.info("Saved file path: " + savePath);

                File thumbFile = new File(uploadPath, "s_" + uuid+"_"+ originalName);

                Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 400,400);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return UploadResultDTO.builder()
                    .uuid(uuid)
                    .fileName(originalName)
                    .build();

        } else {
            return null;
        }
    }


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);

        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> removeFile(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            File thumbnailFile = new File(uploadPath+File.separator +"s_" + fileName);
            thumbnailFile.delete();


        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }


}
