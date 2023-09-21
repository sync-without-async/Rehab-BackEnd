package com.hallym.rehab.domain.room.entity;

import com.hallym.rehab.domain.admin.entity.Admin;
import com.hallym.rehab.domain.room.dto.RoomResponseDTO;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseTimeEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)") // UUID를 이진 형태로 저장하도록 지정
    private UUID rno;

    @OneToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "mid")
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "mid")
    private Member user;

    @ColumnDefault("false") //삭제 여부
    private boolean is_deleted;

    public boolean is_deleted() {
        return is_deleted;
    }

    public void delete() {
        this.is_deleted = true;
    }

    public void revertDelete() {
        this.is_deleted = false;
    }

    public RoomResponseDTO toRoomResponseDTO() {
        return RoomResponseDTO.builder()
                .rno(this.getRno())
                .admin_id(this.getAdmin().getMid())
                .user_id(this.getUser().getMid())
                .build();
    }
}
