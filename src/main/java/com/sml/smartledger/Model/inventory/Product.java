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
    @JsonIgnore
    Business business;
    @Enumerated(EnumType.ORDINAL)
    UnitType unitType;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product", fetch = FetchType.EAGER)
    List<ProductTransaction> productTransactions;
    Date date;
    Double totalStockValue;
}
