package com.sml.smartledger.Model.inventory;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "services")
@Getter
@Setter
public class Service extends BaseModel {
    String name;
    double salePrice;
    int monthlySales;
    int totalSales;
    @ManyToOne(cascade = CascadeType.MERGE)
    Business business;
    @OneToMany(cascade = CascadeType.ALL)

    List<ServiceTransaction> serviceTransactions;


}
