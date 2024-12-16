package com.sml.smartledger.Services.interfaces.party;


import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;

import java.util.List;

public interface PartyTransactionService {

    List<PartyTransaction> getAllTransactions(Long partyId);
    List<PartyTransaction> getAllTransactionsByPartyIds(List<Party> partyIds);


    PartyTransaction createTransaction(PartyTransaction partyTransaction);

    void deleteTransaction(Long id);

    PartyTransaction updateTransaction(PartyTransaction partyTransaction);

    PartyTransaction getTransactionById(Long partyId);
}
