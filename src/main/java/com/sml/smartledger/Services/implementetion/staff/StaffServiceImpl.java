package com.sml.smartledger.Services.implementetion.staff;


import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.staff.StaffMember;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.staff.StaffRepository;
import com.sml.smartledger.Services.interfaces.staff.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    StaffRepository staffRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Override
    public List<StaffMember> getAllStaffMembers(Long businessId) {
        return staffRepository.findAllByBusinessId(businessId);
    }

    @Override
    public StaffMember addNewStaffMember(StaffMember staffMember) {
        Optional<Business> businessOptional = businessRepository.findById(staffMember.getBusiness().getId());
        if(businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        StaffMember savedStaffMember = staffRepository.save(staffMember);
        business.getStaffList().add(savedStaffMember);
        businessRepository.save(business);
        return savedStaffMember;
    }

    @Override
    public StaffMember updateStaffMemberDetails(StaffMember staffMember) {
        return null;
    }

    @Override
    public void deleteStaffMemberDetails(Long staffMemberId) {
        staffRepository.deleteById(staffMemberId);
    }
}
