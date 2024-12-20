package com.sml.smartledger.Model.inventory;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "ProductTransaction")
@Getter
@Setter
public class ProductTransaction extends BaseModel {

    Date date;
    @Enumerated(EnumType.ORDINAL)
    StockTransactionType stockTransactionType;
    double amount;
    int unit;
    String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Product product;
}
