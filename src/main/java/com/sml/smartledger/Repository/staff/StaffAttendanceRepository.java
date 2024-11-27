package com.sml.smartledger.Repository.staff;

import com.sml.smartledger.Model.staff.StaffAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance,Long> {

    List<StaffAttendance> findAllStaffAttendanceByStaffMemberId(Long staffId);
}
