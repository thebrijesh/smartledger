package com.sml.smartledger.Forms;

import com.sml.smartledger.Model.inventory.UnitType;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductTransactionForm {
    Long id;
    Long productId;
    Double amount;
    int stockQuantity;
    String transactionType;
    String description;
    String date;
}
