package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "member")
public class Program extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long pno;

    @Column(name = "title")
    private String programTitle;

    @Enumerated(EnumType.STRING)
    private Category Category;

    private String description;

    @ManyToOne
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private Set<ProgramVideo> programVideo = new HashSet<>();


}
