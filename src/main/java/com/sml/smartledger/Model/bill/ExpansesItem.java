package com.sml.smartledger.Model.bill;


import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "ExpansesItem")
@Getter
@Setter
public class ExpansesItem extends BaseModel {
    String name;
    double price;
    @ManyToOne(cascade = CascadeType.MERGE)
    Business business;
}