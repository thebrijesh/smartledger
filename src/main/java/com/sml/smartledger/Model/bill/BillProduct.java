package com.sml.smartledger.Model.bill;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.UnitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "billproduct")
@Getter
@Setter
public class BillProduct extends BaseModel {

    String name;
    double salePrice;
    double purchasePrice;

    int stockQuantity;
    int lowStock;
    @ManyToOne(cascade = CascadeType.MERGE)
    Business business;
    @Enumerated(EnumType.ORDINAL)
    UnitType unitType;
    @OneToMany(cascade = CascadeType.ALL)
            @JsonIgnore
    List<ProductTransaction> productTransactions;
}
