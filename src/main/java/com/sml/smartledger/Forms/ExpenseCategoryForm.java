package com.sml.smartledger.Forms;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ExpenseCategoryForm {
    private Long id;
    private String name;
    private String description;
}
