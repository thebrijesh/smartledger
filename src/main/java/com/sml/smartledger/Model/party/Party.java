package com.sml.smartledger.Model.party;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "party")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Party extends BaseModel {

    String name;
    String mobile;
    String image;
    @Enumerated(EnumType.ORDINAL)
    PartyType partyType;
    Date dueDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
    Business business;

    String houseNumber;
    String area;
    String pincode;
    String city;
    String state;
    String gstIN;
    Double balance;

    @Builder.Default
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<PartyTransaction> partyTransactionList = new ArrayList<>();

    @Override
    public String toString() {
        return "Party{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", image='" + image + '\'' +
                ", partyType=" + partyType +
                ", dueDate=" + dueDate +
                ", businessId=" + (business != null ? business.getId() : null) +
                ", houseNumber='" + houseNumber + '\'' +
                ", area='" + area + '\'' +
                ", pincode='" + pincode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", gstIN='" + gstIN + '\'' +
                ", balance=" + balance +
                '}';
    }
}