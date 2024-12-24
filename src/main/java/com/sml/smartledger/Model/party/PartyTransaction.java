package com.sml.smartledger.Model.party;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "PartyTransaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyTransaction extends BaseModel implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Party party;
    double amount;
    Date date;
    String description;
    Date transactionDate;
    @Enumerated(EnumType.ORDINAL)
    TransactionType transactionType;


}
