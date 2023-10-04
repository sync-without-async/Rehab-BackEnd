package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    @Query("SELECT p from Program p where p.user.mid = :userId")
    Optional<Program> findByUserId(@Param("userId") String userId);
}
