package com.sml.smartledger.Model.party;

import com.sml.smartledger.Model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "PartyTransaction")
@Getter
@Setter
public class PartyTransaction  extends BaseModel {
    @ManyToOne(cascade = CascadeType.MERGE)
    Party party;
    double amount;
    Date date;
    @Enumerated(EnumType.ORDINAL)
    TransactionType transactionType;
}
