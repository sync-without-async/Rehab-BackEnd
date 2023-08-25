package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramVideoRepository extends JpaRepository<ProgramVideo, Long> {
    List<ProgramVideo> findByProgram(Program program);
}
