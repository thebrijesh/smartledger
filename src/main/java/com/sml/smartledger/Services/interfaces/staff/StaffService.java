package com.sml.smartledger.Services.interfaces.staff;


import com.sml.smartledger.Model.staff.StaffMember;

import java.util.List;

public interface StaffService {
    List<StaffMember> getAllStaffMembers(Long businessId);
    StaffMember addNewStaffMember(StaffMember staffMember);
    StaffMember updateStaffMemberDetails(StaffMember staffMember);
    void deleteStaffMemberDetails(Long staffMemberId);
}
