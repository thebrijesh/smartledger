package com.sml.smartledger.Model.bill;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "ExpensesCategory")
@Getter
@Setter
public class ExpensesCategory extends BaseModel {
    @Column(nullable = false,unique = true)
    String name;
    String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
    Business business;
    @OneToMany(mappedBy = "expensesCategory", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    List<Expenses> expenses = new ArrayList<>();

}
