package com.sml.smartledger.Model.bill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Model.inventory.ServiceTransaction;
import com.sml.smartledger.Model.party.Party;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity(name = "bill")
@Getter
@Setter
public class Bill extends BaseModel {
    @ManyToOne
    Party party;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Business business;
    double amount;
    double amountDue;
    Date date;

    Date dueDate;
    @Enumerated(EnumType.ORDINAL)
    PaymentType paymentType;
    @Enumerated(EnumType.ORDINAL)
    BillType billType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<ProductTransaction> productTransactions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<ServiceTransaction> serviceTransactions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<AdditionalCharges> additionalCharges;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<CustomFields> customFields;

    @ElementCollection
    List<String> termsAndConditions;
}