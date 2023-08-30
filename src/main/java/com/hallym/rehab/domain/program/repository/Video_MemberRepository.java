package com.hallym.rehab.domain.program.repository;

import com.hallym.rehab.domain.program.entity.Video_Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Video_MemberRepository extends JpaRepository<Video_Member, Long> {
    @Query("select vm from Video_Member vm where vm.member.mid = :mid and vm.video.vno = :vno")
    Optional<Video_Member> findByMemberAndVideo(@Param("mid") String mid, @Param("vno") Long vno);
}
