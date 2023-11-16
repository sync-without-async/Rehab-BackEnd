package com.hallym.rehab.domain.user.entity;

import com.hallym.rehab.domain.reservation.entity.Time;
import com.hallym.rehab.domain.video.entity.Video;
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
@ToString(exclude = "roleSet")
@Entity
public class Staff extends BaseTimeEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String mid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String hospital;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @OneToOne(mappedBy = "staff",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private StaffImage staffImage;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>(); //권한 정보

    @Builder.Default
    @OneToMany(mappedBy = "vno", cascade = CascadeType.ALL)
    private Set<Video> videoList = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "tno", cascade = CascadeType.ALL)
    private Set<Time> timeList = new HashSet<>();

    @ColumnDefault("false")
    private boolean is_deleted;

    public void addRole(MemberRole memberRole) {
        this.roleSet.add(memberRole);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void addStaff(String mid, String password, String name, String phone, Set<MemberRole> roleSet) {
        this.mid = mid;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.roleSet = roleSet;
    }

    public void addImage(String fileName) {

        this.staffImage = StaffImage.builder()
                .profileUrl(fileName)
                .staff(this) //양방향의 경우 참조 관계가 서로 일치하도록
                .build();

    }

    public void clearImage() {
        if (this.staffImage != null) {

            this.staffImage.changeStaff(null);
            this.staffImage = null;
        }
    }
}
