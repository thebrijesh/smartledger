package com.sml.smartledger.Services.interfaces.party;


import com.sml.smartledger.Model.party.PartyTransaction;

import java.util.List;

public interface PartyTransactionService {

    List<PartyTransaction> getAllTransaction(Long partyId);
    PartyTransaction createTransaction(PartyTransaction partyTransaction);

    void deleteTransaction(Long id);

    PartyTransaction updateTransaction(PartyTransaction partyTransaction);

    PartyTransaction getTransactionById(Long partyId);
}
