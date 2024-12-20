package com.sml.smartledger.Model.bill;

import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.Service;
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
    Business business;
    double amount;
    Date date;
    @Enumerated(EnumType.ORDINAL)
    PaymentType paymentType;
    @Enumerated(EnumType.ORDINAL)
    BillType billType;
    @ElementCollection
    @CollectionTable(name = "bill_products", joinColumns = @JoinColumn(name = "bill_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    Map<Product, Integer> products;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Service> services;
}