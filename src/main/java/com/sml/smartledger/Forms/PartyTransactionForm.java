package com.sml.smartledger.Forms;

import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PartyTransactionForm {
    Long id;
    Long partyId;
    Double amount;
    String transactionType;
    String description;
    String date;
}
