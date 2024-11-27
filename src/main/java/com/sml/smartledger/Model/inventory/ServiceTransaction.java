package com.sml.smartledger.Model.inventory;


import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.bill.BillService;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne
    BillService billService;
    @Enumerated(EnumType.ORDINAL)
    ServiceTransactionType type;
}
