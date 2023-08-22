package com.hallym.rehab.domain.user.repository;

import com.hallym.rehab.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
