package com.hallym.rehab.domain.user.repository;

import com.hallym.rehab.domain.user.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PatientRepository extends JpaRepository<Patient, String> {

    @Modifying
    @Transactional
    @Query("update Patient m set m.password =:password where m.mid =:mid")
    void updatePassword(@Param("mid") String mid, @Param("password") String password);
}
