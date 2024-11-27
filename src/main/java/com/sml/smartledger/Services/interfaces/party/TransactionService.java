package com.sml.smartledger.Services.interfaces.party;


import com.sml.smartledger.Model.party.PartyTransaction;

import java.util.List;

public interface TransactionService {

    List<PartyTransaction> getAllTransaction(Long partyId);
    PartyTransaction createTransaction(PartyTransaction partyTransaction);
}
