package com.sml.smartledger.Forms;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTransactionForm {

    Long id;
    Long serviceId;
    Double amount;
    int stockQuantity;
    String transactionType;
    String description;
    String date;
}
