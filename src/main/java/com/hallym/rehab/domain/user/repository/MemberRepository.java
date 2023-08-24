package com.hallym.rehab.domain.user.repository;

import com.hallym.rehab.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("SELECT m FROM Member m WHERE m.mid = :mid") //ID에 해당하는 사용자 정보 반환
    Member findByUserId(@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.password =:password where m.mid =:mid")
    void updatePassword(@Param("mid") String mid, @Param("password") String password);
}
