package com.sml.smartledger.Model.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.BillProduct;
import com.sml.smartledger.Model.bill.BillService;
import com.sml.smartledger.Model.bill.Expanses;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.staff.StaffMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "business")
public class Business extends BaseModel {
    String name;
    String mobile;
    String address;
    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;
    @OneToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    List<StaffMember> staffList;
    @OneToMany
    List<Party> parties;
    @OneToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    List<Bill> bills = new ArrayList<>();
    @OneToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    List<Expanses> expansesList;
    @OneToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    List<BillProduct> products;
    @OneToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    List<BillService> services;

    double totalCredit;
    double totalDebit;
}

