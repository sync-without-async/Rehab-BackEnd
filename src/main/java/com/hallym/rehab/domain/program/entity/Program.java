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
@ToString(exclude = "programHistory")
@Entity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private Set<Video> video = new HashSet<>(); // 한 프로그램에 어떤 운동 동작 비디오들이 있는지

    @Builder.Default
    @JsonBackReference
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private Set<ProgramHistory> programHistory = new HashSet<>(); // 유저 프로그램 신청 여부

    @ColumnDefault("false") //삭제 여부
    private boolean is_deleted;

    public void modifyProgram(String programTitle, String description, Category category, Position position){
        this.programTitle = programTitle;
        this.description = description;
        this.category = category;
        this.position = position;
    }

    public void addVideo(Video video) {
        video.setProgram(this);
        this.video.add(video);
    }

    public void deleteVideo(Video video) {
        video.setProgram(null);
        this.video.remove(video);
    }

    public void setIs_deleted(Boolean is_deleted){ //soft delete
        this.is_deleted = is_deleted;
    }
}
