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
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity(name = "business")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Business extends BaseModel {
    String name;
    String mobile;
    String address;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<StaffMember> staffList = new ArrayList<>();
    @Builder.Default
    @OneToMany
    List<Party> parties= new ArrayList<>();
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<Bill> bills = new ArrayList<>();
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<Expanses> expansesList= new ArrayList<>();
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<BillProduct> products= new ArrayList<>();
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<BillService> services= new ArrayList<>();
    private String logo;

    double totalCredit;
    double totalDebit;
}

