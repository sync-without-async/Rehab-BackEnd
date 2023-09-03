package com.hallym.rehab.domain.program.controller;

import com.hallym.rehab.domain.program.dto.video.ChangeOrdRequestDTO;
import com.hallym.rehab.domain.program.dto.video.VideoRequestDTO;
import com.hallym.rehab.domain.program.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth/video")
@RequiredArgsConstructor
public class VideoAdminController {

    private final VideoService videoService;

    @PostMapping("/create/{pno}/{ord}")
    public ResponseEntity<String> createVideo(@PathVariable Long pno,
                                              @PathVariable Long ord,
                                              VideoRequestDTO videoRequestDTO) {
        String result = videoService.createVideo(pno, ord, videoRequestDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/modify/{pno}")
    public ResponseEntity<String> changeVideoOrd(@PathVariable Long pno,
                            @RequestBody ChangeOrdRequestDTO changeOrdRequestDTO) {
        String result = videoService.changeVideoOrd(pno, changeOrdRequestDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{pno}/{ord}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long pno,
                                              @PathVariable Long ord) {
        String result = videoService.deleteVideo(pno, ord);
        return ResponseEntity.ok(result);
    }


}
