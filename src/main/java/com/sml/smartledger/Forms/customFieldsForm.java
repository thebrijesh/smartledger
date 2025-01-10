package com.sml.smartledger.Forms;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class customFieldsForm {

    private String name;
    private String value;
    private Long id;
}
