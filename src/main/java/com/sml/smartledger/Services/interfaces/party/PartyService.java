package com.sml.smartledger.Services.interfaces.party;


import com.sml.smartledger.Model.party.Party;
import lombok.NonNull;

import java.util.List;

public interface PartyService {

    public Party createParty(Party party);

    public void deleteParty(Long id);
    public List<Party> getAllParty(Long BusinessId);

    List<Party> getCustomerParty(@NonNull Long id);


    Party getPartyById(Long partyId);
}
