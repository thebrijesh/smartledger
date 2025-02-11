package com.sml.smartledger.Model.bill;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity(name = "expense")
@Getter
@Setter
@ToString
public class Expenses extends BaseModel {

    String name;
    Date date;
    @ManyToOne(cascade = CascadeType.ALL)
    ExpensesCategory expensesCategory;
    double amount;
    @OneToMany
    List<ExpensesItem> expensesItems;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Business business;
    String description;

}
