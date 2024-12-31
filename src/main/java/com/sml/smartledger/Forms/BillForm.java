package com.sml.smartledger.Forms;

import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BillForm {

    private Long partyId;
    private double amount;
    private String date;
    private String paymentType;
    private String billType;
    private Map<Long, ProductTransaction> products;
    private Map<Long, ServiceTransaction> services;

}
