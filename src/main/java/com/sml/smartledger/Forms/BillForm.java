package com.sml.smartledger.Forms;

import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BillForm {

    private Long id;
    private Long partyId;
    private double amount;
    private String date;
    private String paymentType;
    private double dueAmount;
    private double paidAmount;
    private List<String> terms;
    private List<chargesForm> additionalCharges;
    private List<customFieldsForm> customFields;
    private double totalDiscount;
    private String discountType;
    private String billType;
    private List<ProductTransactionForm> products;
    private List<ServiceTransactionForm> services;

}
