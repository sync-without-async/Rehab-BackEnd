package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.ProgramVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ProgramVideoRepository extends JpaRepository<ProgramVideo, Long> {

    @Query("select pv from Program p join p.programVideo pv where p.pno = :pno and pv.ord = :ord")
    Optional<ProgramVideo> findByPno(Long pno, Long ord);
}
