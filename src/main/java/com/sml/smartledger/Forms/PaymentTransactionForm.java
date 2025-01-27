package com.sml.smartledger.Forms;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class PaymentTransactionForm {
    long partyId;
    double amount;
    String date;
    String description;
    String paymentMode;
    String paymentType;
    List<Long> bills;
}
