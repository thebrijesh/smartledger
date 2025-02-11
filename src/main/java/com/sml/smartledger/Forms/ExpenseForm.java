package com.sml.smartledger.Forms;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ExpenseForm {
    private Long id;
    private String name;
    private String date;
    private Long expensesCategoryId;
    private double amount;
    private String description;
}
