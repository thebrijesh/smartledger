package com.sml.smartledger.Forms;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class chargesForm {

    private String name;
    private double amount;
    private Long id;
}
