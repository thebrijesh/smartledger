package com.sml.smartledger.Model.bill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.*;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity(name = "bill")
@Getter
@Setter
public class Bill extends BaseModel {
    @ManyToOne
    @JsonIgnore
    Party party;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Business business;
    double amount;
    double amountDue;
    Date date;

    double discount;
    @Enumerated(EnumType.ORDINAL)
    DiscountType discountType;

    Date dueDate;
    @Enumerated(EnumType.ORDINAL)
    PaymentType paymentType;
    @Enumerated(EnumType.ORDINAL)
    BillType billType;
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<ProductTransaction> productTransactions;

    @OneToMany(mappedBy = "bill", cascade =  CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<ServiceTransaction> serviceTransactions;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<AdditionalCharges> additionalCharges;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<CustomFields> customFields;

    @ElementCollection
    List<String> termsAndConditions;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    PartyTransaction transaction;
}