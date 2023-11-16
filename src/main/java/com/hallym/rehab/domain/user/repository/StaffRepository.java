package com.hallym.rehab.domain.user.repository;

import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, String> {

    @EntityGraph(attributePaths = {"staffImage"})
    @Query("select s from Staff s where s.mid =:mid")
    Staff findByIdWithImages(@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("UPDATE Staff m SET m.password =:password WHERE m.mid =:mid")
    void updatePassword(@Param("mid") String mid, @Param("password") String password);

    @Query("SELECT s FROM Staff s WHERE :role MEMBER OF s.roleSet ORDER BY s.department ASC")
    List<Staff> findTherapists(@Param("role") MemberRole role);


}
