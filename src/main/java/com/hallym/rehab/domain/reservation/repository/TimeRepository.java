package com.hallym.rehab.domain.reservation.repository;

import com.hallym.rehab.domain.reservation.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeRepository extends JpaRepository<Time, Long> {

    @Query("SELECT t FROM Time t WHERE t.staff.mid = :staff_id")
    List<Time> findByStaff(@Param("staff_id") String staff_id);

    @Query("SELECT t FROM Time t WHERE t.staff.mid = :staff_id " +
            "and t.date = :date and t.index = :index")
    Optional<Time> findReservation(@Param("staff_id") String staff_id,
                             @Param("date") LocalDate localDate,
                             @Param("index") int index);

    @Query("SELECT t FROM Time t WHERE t.staff.mid = :staff_id " +
            "and t.date = :date")
    List<Time> findAvailableTimeOfDayByStaff(@Param("staff_id") String staff_id,
                                             @Param("date") LocalDate localDate);
}
