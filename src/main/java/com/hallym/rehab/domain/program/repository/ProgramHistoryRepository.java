package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramHistoryRepository extends JpaRepository<ProgramHistory, Long> {

    @Query("select pm from programHistory pm where pm.member.mid = :mid and pm.program.pno = :pno")
    Optional<ProgramHistory> findByMemberWithProgram(@Param("mid") String mid, @Param("pno") Long pno);

    @Query("SELECT pm.program FROM programHistory pm WHERE pm.member.mid = :mid")
    List<Program> searchProgramListByMid(@Param("mid") String mid);
}
