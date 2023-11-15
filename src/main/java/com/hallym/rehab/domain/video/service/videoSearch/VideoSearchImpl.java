package com.hallym.rehab.domain.video.service.videoSearch;

import com.hallym.rehab.domain.video.dto.QVideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.entity.QVideo;
import com.hallym.rehab.domain.video.entity.Tag;
import com.hallym.rehab.domain.video.entity.Video;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
public class VideoSearchImpl extends QuerydslRepositorySupport implements VideoSearch {

    private final JPAQueryFactory queryFactory;

    public VideoSearchImpl(EntityManager entityManager) {
        super(Video.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<VideoResponseDTO> search(VideoPageRequestDTO requestDTO) {
        QVideo video = QVideo.video;

        String title = requestDTO.getTitle();
        Tag tag = requestDTO.getTag();

        Pageable pageable = requestDTO.getPageable();

        // BooleanBuilder로 조건 추가
        BooleanBuilder builder = new BooleanBuilder();
        if (title != null) builder.and(video.title.containsIgnoreCase(title));
        if (tag != null) builder.and(video.tag.eq(tag));

        QueryResults<VideoResponseDTO> fetchResults = queryFactory
                .select(new QVideoResponseDTO(video.vno, video.title, video.description,
                        video.tag, video.playTime, video.videoURL, video.thumbnailURL))
                .from(video)
                .where(builder)
                .orderBy(video.vno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = fetchResults.getTotal();
        List<VideoResponseDTO> content = fetchResults.getResults();

        return new PageImpl<>(content, pageable, total);
    }
}
