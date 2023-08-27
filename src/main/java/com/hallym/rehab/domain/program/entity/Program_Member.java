package com.hallym.rehab.domain.program.entity;

import com.hallym.rehab.domain.user.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Program_Member {
    /**
     * 유저마다 어떤 프로그램에 수강등록 되어있는지 확인하는 테이블
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pmno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno")
    private Program program;
}
