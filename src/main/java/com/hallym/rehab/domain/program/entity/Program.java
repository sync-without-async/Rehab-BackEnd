package com.hallym.rehab.domain.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Builder.Default
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private Set<ProgramVideo> programVideo = new HashSet<>();

    @ManyToOne
    private Member member;

    @ColumnDefault("false") //삭제 여부
    private boolean is_deleted;

    public void modifyProgram(String programTitle, String description, Category category, Position position){
        this.programTitle = programTitle;
        this.description = description;
        this.category = category;
        this.position = position;
    }

    public void addProgramVideo(String GuideVideoPath, String JsonPath){} //비디오 파일 추가 메소드

    public void clearProgramVideo(){ //비디오 파일 변경 시 사용할 메소드

        programVideo.forEach(programVideo -> programVideo.changeProgram(null));

        this.programVideo.clear();
    }

    public void setIs_deleted(Boolean is_deleted){ //soft delete
        this.is_deleted = is_deleted;
    }
}
