package com.sml.smartledger.Model.bill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity(name = "AdditionalCharges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdditionalCharges  extends BaseModel {

    String name;
    double amount;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Bill bill;


}
