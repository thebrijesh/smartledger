package com.sml.smartledger.Model.inventory;


import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity(name = "products")
@Getter
@Setter
public class Product extends BaseModel {

    String name;
    String image;
    double salePrice;
    double purchasePrice;
    private String cloudinaryImagePublicId;
    int stockQuantity;
    int lowStock;
    @ManyToOne(cascade = CascadeType.ALL)
    Business business;
    @Enumerated(EnumType.ORDINAL)
    UnitType unitType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.EAGER)
    List<ProductTransaction> productTransactions;
    Date date;
}
