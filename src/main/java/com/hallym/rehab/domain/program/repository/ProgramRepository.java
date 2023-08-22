package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {


}
