package com.sml.smartledger.Services.implementetion.party;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.TransactionType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.party.PartyRepository;
import com.sml.smartledger.Repository.party.TransactionRepository;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyTransactionServiceImpl implements PartyTransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    PartyRepository partyRepository;
    @Override
    public List<PartyTransaction> getAllTransaction(Long partyId) {
        return transactionRepository.findAllByPartyId(partyId);
    }

    @Override
    public PartyTransaction createTransaction(PartyTransaction partyTransaction) {
        Optional<Party> partyOptional = partyRepository.findById(partyTransaction.getParty().getId());
        if(partyOptional.isEmpty()) throw new RuntimeException("Party not found");
        Optional<Business> businessOptional = businessRepository.findById(partyOptional.get().getBusiness().getId());
        if(businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();
        if(partyTransaction.getTransactionType() == TransactionType.CREDIT){
            business.setTotalCredit(business.getTotalCredit()+ partyTransaction.getAmount());
        }else {
            business.setTotalDebit(business.getTotalDebit()+ partyTransaction.getAmount());
        }
        businessRepository.save(business);
        return transactionRepository.save(partyTransaction);
    }
}
