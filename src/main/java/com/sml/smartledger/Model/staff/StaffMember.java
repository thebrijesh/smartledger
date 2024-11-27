package com.sml.smartledger.Model.staff;


import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "staffMember")
@Getter
@Setter
public class StaffMember extends BaseModel {

    String name;
    String mobile;
    @ManyToOne(cascade = CascadeType.MERGE)
    Business business;
    @ElementCollection(targetClass = MemberPermission.class)
    @Enumerated(EnumType.ORDINAL)
    List<MemberPermission> permissionList;
    int presentDays;
    int paidLeave;
    int halfDay;
    double totalDue;

}
