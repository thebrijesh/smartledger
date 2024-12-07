package com.sml.smartledger.Services.implementetion.party;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.TransactionType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.party.PartyRepository;
import com.sml.smartledger.Repository.party.TransactionRepository;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PartyTransactionServiceImpl implements PartyTransactionService {
    final
    TransactionRepository transactionRepository;
    final
    BusinessRepository businessRepository;
    final
    PartyRepository partyRepository;
    Logger logger = LoggerFactory.getLogger(PartyTransactionServiceImpl.class);

    @Autowired
    public PartyTransactionServiceImpl(TransactionRepository transactionRepository, BusinessRepository businessRepository, PartyRepository partyRepository) {
        this.transactionRepository = transactionRepository;
        this.businessRepository = businessRepository;
        this.partyRepository = partyRepository;
    }

    @Override
    public List<PartyTransaction> getAllTransaction(Long partyId) {
        return transactionRepository.findAllByPartyId(partyId);
    }

    @Override
    public PartyTransaction createTransaction(PartyTransaction partyTransaction) {
        Optional<Party> partyOptional = partyRepository.findById(partyTransaction.getParty().getId());
        if (partyOptional.isEmpty()) throw new RuntimeException("Party not found");
        Optional<Business> businessOptional = businessRepository.findById(partyOptional.get().getBusiness().getId());
        if (businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();
        logger.info("Balance {} ", partyOptional.get().getBalance());
        logger.info("Amount {} ", partyTransaction.getAmount());
        if (partyTransaction.getTransactionType() == TransactionType.CREDIT) {
            business.setTotalCredit(business.getTotalCredit() + partyTransaction.getAmount());
            partyOptional.get().setBalance(partyOptional.get().getBalance() - partyTransaction.getAmount());

        } else {
            business.setTotalDebit(business.getTotalDebit() + partyTransaction.getAmount());
            partyOptional.get().setBalance(partyOptional.get().getBalance() + partyTransaction.getAmount());
        }
        if(partyTransaction.getTransactionDate() == null){
            partyTransaction.setTransactionDate(new Date());
        }

        businessRepository.save(business);
        partyRepository.save(partyOptional.get());
        return transactionRepository.save(partyTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public PartyTransaction updateTransaction(PartyTransaction partyTransaction) {
        return transactionRepository.save(partyTransaction);
    }

    @Override
    public PartyTransaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}
