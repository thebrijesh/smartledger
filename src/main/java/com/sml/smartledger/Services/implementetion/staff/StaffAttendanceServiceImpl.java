package com.sml.smartledger.Services.implementetion.staff;


import com.sml.smartledger.Model.staff.StaffAttendance;
import com.sml.smartledger.Repository.staff.StaffAttendanceRepository;
import com.sml.smartledger.Services.interfaces.staff.StaffAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffAttendanceServiceImpl implements StaffAttendanceService {
    @Autowired
    StaffAttendanceRepository staffAttendanceRepository;
    @Override
    public StaffAttendance addStaffAttendance(StaffAttendance staffAttendance) {
        return staffAttendanceRepository.save(staffAttendance);
    }

    public List<StaffAttendance> getStaffAttendance(Long staffId) {
        return staffAttendanceRepository.findAllStaffAttendanceByStaffMemberId(staffId);
    }
}
