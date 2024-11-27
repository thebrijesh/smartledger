package com.sml.smartledger.Services.interfaces.staff;


import com.sml.smartledger.Model.staff.StaffAttendance;

import java.util.List;

public interface StaffAttendanceService {
    StaffAttendance addStaffAttendance(StaffAttendance staffAttendance);
    List<StaffAttendance> getStaffAttendance(Long staffId);
}
