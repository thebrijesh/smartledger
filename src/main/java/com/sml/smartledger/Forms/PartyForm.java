package com.sml.smartledger.Forms;

import com.sml.smartledger.Model.party.PartyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PartyForm {
    String name;
    String number;

    Double balance ;
    String partyType;
    String transectionType;
    String gstIN;
    String houseNumber;
    String area;
    String pincode;
    String city;
    String state;
}
