package com.sml.smartledger.Model.bill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomFields extends BaseModel {
    String name;
    String value;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Bill bill;
}
