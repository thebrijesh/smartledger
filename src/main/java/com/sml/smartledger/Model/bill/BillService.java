package com.sml.smartledger.Model.bill;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "billService")
@Getter
@Setter
public class BillService extends BaseModel {
    String name;
    double salePrice;
    int monthlySales;
    int totalSales;
    @ManyToOne(cascade = CascadeType.MERGE)
    Business business;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<ServiceTransaction> serviceTransactions;


}
