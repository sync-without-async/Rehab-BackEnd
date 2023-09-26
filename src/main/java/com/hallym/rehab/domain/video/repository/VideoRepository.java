package com.hallym.rehab.domain.video.repository;

import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.service.videoSearch.VideoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoSearch {
}
