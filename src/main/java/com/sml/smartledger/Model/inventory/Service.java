package com.sml.smartledger.Model.inventory;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity(name = "services")
@Getter
@Setter
@ToString
public class Service extends BaseModel {
    String name;
    String image;
    private String cloudinaryImagePublicId;
    double servicePrice;
    int totalSoldUnits;
    @ManyToOne(cascade = CascadeType.MERGE)
    Business business;
    @Enumerated(EnumType.ORDINAL)
    UnitType unitType;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "service", fetch = FetchType.EAGER)
    List<ServiceTransaction> serviceTransactions;

    Date date;




}
