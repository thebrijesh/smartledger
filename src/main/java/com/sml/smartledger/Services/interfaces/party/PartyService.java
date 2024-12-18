package com.sml.smartledger.Services.interfaces.party;


import com.sml.smartledger.Model.party.Party;
import lombok.NonNull;

import java.util.List;

public interface PartyService {

     Party createParty(Party party);

     void deleteParty(Long id);
     List<Party> getAllParties(Long BusinessId);

    List<Party> getCustomerParties(@NonNull Long id);


    Party getPartyById(Long partyId);

    void updateParty(Party party);

    List<Party> getSupplierParties(@NonNull Long id);

    Party getPartyByShortCode(String shortCode);

    List<Party> getDueParties(@NonNull Long id);
}
