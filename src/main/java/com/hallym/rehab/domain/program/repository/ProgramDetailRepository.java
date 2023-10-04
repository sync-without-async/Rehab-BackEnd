package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.dto.ProgramResponseDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramDetail;
import com.hallym.rehab.domain.user.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProgramDetailRepository extends JpaRepository<ProgramDetail, Long> {

    @Query("SELECT pd FROM ProgramDetail pd WHERE pd.program.pno = :pno and pd.video.vno = :vno and pd.ord = :ord")
    Optional<ProgramDetail> findByInfo(@Param("pno") Long pno, @Param("vno") Long vno, @Param("ord") int ord);

    @Query("SELECT pd FROM ProgramDetail pd WHERE pd.program.pno = :pno and pd.ord = :ord")
    Optional<ProgramDetail> findByPnoAndOrd(@Param("pno") Long pno, @Param("ord") int ord);

    @Query("SELECT new com.hallym.rehab.domain.program.dto." +
            "ProgramResponseDTO(pd.video.title, pd.ord, pd.video.vno, pd.videoMetrics.metrics) " +
            "FROM ProgramDetail pd WHERE pd.program = :program ORDER BY pd.ord")
    Page<ProgramResponseDTO> findPageByProgram(@Param("program") Program program, Pageable pageable);
}
