package com.sml.smartledger.Model.staff;


import com.sml.smartledger.Model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "StaffAttendance")
@Getter
@Setter
public class StaffAttendance extends BaseModel {
    @ManyToOne
    StaffMember staffMember;
    @Enumerated(EnumType.ORDINAL)
    StaffAttendanceType type;
    Date date;
}
