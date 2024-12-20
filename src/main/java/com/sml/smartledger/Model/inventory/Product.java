package com.sml.smartledger.Model.inventory;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "products")
@Getter
@Setter
public class Product extends BaseModel {

    String name;
    double salePrice;
    double purchasePrice;

    int stockQuantity;
    int lowStock;
    @ManyToOne(cascade = CascadeType.ALL)
    Business business;
    @Enumerated(EnumType.ORDINAL)
    UnitType unitType;
    @OneToMany(cascade = CascadeType.ALL)
    List<ProductTransaction> productTransactions;
}
