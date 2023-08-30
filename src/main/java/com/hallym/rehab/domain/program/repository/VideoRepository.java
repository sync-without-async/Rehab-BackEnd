package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("select pv from Program p join p.video pv where p.pno = :pno and pv.ord = :ord")
    Optional<Video> findByPnoAndOrd(@Param("pno") Long pno, @Param("ord") Long ord);

    @Query("select pv from Program p join p.video pv where p.pno = :pno")
    List<Video> findByPno(@Param("pno") Long pno);
}
