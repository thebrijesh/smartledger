package com.sml.smartledger.Controller.staff;


import com.sml.smartledger.Model.staff.StaffMember;
import com.sml.smartledger.Services.interfaces.staff.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffService staffService;

    @GetMapping("/getAllStaff/{businessId}")
    public ResponseEntity<List<StaffMember>> getAllStaffByBusinessId(@PathVariable("businessId") Long businessId) {
        List<StaffMember> staffMemberList = staffService.getAllStaffMembers(businessId);
        return new ResponseEntity<>(staffMemberList, HttpStatus.OK);
    }

    @PostMapping("/addStaff")
    public ResponseEntity<StaffMember> addNewStaff(@RequestBody StaffMember staffMember) {
        StaffMember savedStaffMember = staffService.addNewStaffMember(staffMember);
        return new ResponseEntity<>(savedStaffMember, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteStaff/{id}")
    public ResponseEntity<Void> deleteStaffMember(@PathVariable("id") Long staffId) {
        staffService.deleteStaffMemberDetails(staffId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
