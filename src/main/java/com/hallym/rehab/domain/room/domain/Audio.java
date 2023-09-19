package com.hallym.rehab.domain.room.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ano;

    @NotNull
    @OneToOne
    private Room room;

    private String summary; // 인공지능이 만들어 낸 음성대화 요약본

    @Column(name = "adminAudioURL")
    private String adminAudioURL; // 동영상 URL로 접근할 URL

    @Column(name = "userAudioURL")
    private String userAudioURL; // JSON 파일 접근할 URL

    @Column(name = "adminAudioObjectPath")
    private String adminAudioObjectPath; // admin audio 삭제하기 위한 ObjectPath

    @Column(name = "userAudioObjectPath")
    private String userAudioObjectPath; // user audio 삭제하기 위한 ObjectPath

    public void setUserAudio(String userAudioURL, String userAudioObjectPath) {
        this.userAudioURL = userAudioURL;
        this.userAudioObjectPath = userAudioObjectPath;
    }

    public void setAdminAudio(String adminAudioURL, String adminAudioObjectPath) {
        this.adminAudioURL = adminAudioURL;
        this.adminAudioObjectPath = adminAudioObjectPath;
    }
}
