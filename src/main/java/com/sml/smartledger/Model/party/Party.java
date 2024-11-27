package com.sml.smartledger.Model.party;


import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Entity(name = "party")
@Getter
@Setter
public class Party extends BaseModel {

    String name;
    String mobile;
    String image;
    @Enumerated(EnumType.ORDINAL)
    PartyType partyType;
    Date dueDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    Business business;
}
