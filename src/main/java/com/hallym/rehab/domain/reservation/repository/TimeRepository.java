package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.reservation.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {

    @Query("SELECT t FROM Time t WHERE t.admin.mid = :admin_id")
    List<Time> findByAdmin(@Param("admin_id") String admin_id);
}
