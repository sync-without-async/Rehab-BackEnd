package com.hallym.rehab.domain.user.repository;

import com.hallym.rehab.domain.user.entity.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface StaffRepository extends JpaRepository<Staff, String> {

    @EntityGraph(attributePaths = {"staffImage"})
    @Query("select s from Staff s where s.mid =:mid")
    Staff findByIdWithImages(@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Staff m set m.password =:password where m.mid =:mid")
    void updatePassword(@Param("mid") String mid, @Param("password") String password);
}
