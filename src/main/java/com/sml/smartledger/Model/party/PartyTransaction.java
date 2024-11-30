package com.sml.smartledger.Model.party;

import com.sml.smartledger.Model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "PartyTransaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyTransaction  extends BaseModel {
    @ManyToOne(cascade = CascadeType.ALL)
    Party party;
    double amount;
    Date date;
    String description;
    @Enumerated(EnumType.ORDINAL)
    TransactionType transactionType;
}
