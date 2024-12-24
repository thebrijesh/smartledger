package com.sml.smartledger.Model.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitModel {
    String unitFullName;
    UnitType unitType;
}
