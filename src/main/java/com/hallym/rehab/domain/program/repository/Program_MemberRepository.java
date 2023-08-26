package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.Program_Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface Program_MemberRepository extends JpaRepository<Program_Member, Long> {

    @Query("select pm from Program_Member pm where pm.member.mid = :mid and pm.program.pno = :pno")
    Optional<Program_Member> findByMemberAndProgram(String mid, Long pno);
}
