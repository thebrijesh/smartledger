package com.sml.smartledger.Model.inventory;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.bill.Bill;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "ServiceTransaction")
@Getter
@Setter
public class ServiceTransaction extends BaseModel {

    Date date;
    double amount;
    int unit;
    String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Service service;
    @Enumerated(EnumType.ORDINAL)
    ServiceTransactionType type;

    DiscountType discountType;
    double discount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Bill bill;
}
