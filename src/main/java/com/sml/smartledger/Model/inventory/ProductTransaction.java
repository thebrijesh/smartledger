package com.sml.smartledger.Model.inventory;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.bill.Bill;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "ProductTransaction")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductTransaction extends BaseModel {

    Date date;
    double amount;
    int unit;
    String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Product product;
    @Enumerated(EnumType.ORDINAL)
    StockTransactionType stockTransactionType;

    DiscountType discountType;
    double discount;
    double discountAmount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Bill bill;

}
