package com.sml.smartledger.Services.implementetion.party;

import com.sml.smartledger.Exception.BusinessNotFoundException;
import com.sml.smartledger.Exception.PartyNotFoundException;
import com.sml.smartledger.Exception.TransactionNotFoundException;
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
    final TransactionRepository transactionRepository;
    final BusinessRepository businessRepository;
    final PartyRepository partyRepository;
    Logger logger = LoggerFactory.getLogger(PartyTransactionServiceImpl.class);

    @Autowired
    public PartyTransactionServiceImpl(TransactionRepository transactionRepository, BusinessRepository businessRepository, PartyRepository partyRepository) {
        this.transactionRepository = transactionRepository;
        this.businessRepository = businessRepository;
        this.partyRepository = partyRepository;
    }

    @Override
    public List<PartyTransaction> getAllTransactions(Long partyId) {
        return transactionRepository.findAllByPartyId(partyId);
    }

    @Override
    public List<PartyTransaction> getAllTransactionsByPartyIds(List<Party> partyIds) {
        return transactionRepository.findAllByPartyIn(partyIds);
    }

    @Override
    @Transactional
    public PartyTransaction createTransaction(PartyTransaction partyTransaction) {
        Party party = partyRepository.findById(partyTransaction.getParty().getId())
                .orElseThrow(() -> new PartyNotFoundException("Party with ID " + partyTransaction.getParty().getId() + " not found"));
        Business business = businessRepository.findById(party.getBusiness().getId())
                .orElseThrow(() -> new BusinessNotFoundException("Business with ID " + party.getBusiness().getId() + " not found"));

        updateBusinessAndPartyBalances(business, party, partyTransaction.getAmount(), partyTransaction.getTransactionType());

        if (partyTransaction.getTransactionDate() == null) {
            partyTransaction.setTransactionDate(new Date());
        }

        businessRepository.save(business);
        partyRepository.save(party);
        return transactionRepository.save(partyTransaction);
    }
    @Transactional
    public void deleteTransaction(Long id) {
//        partyRepository.deleteById(id);
        PartyTransaction partyTransaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        Business business = partyTransaction.getParty().getBusiness();
        Party party = partyTransaction.getParty();
        if(party.getBalance() < 0){
            business.setTotalCredit(business.getTotalCredit() + (party.getBalance()));
        }else{
            business.setTotalDebit(business.getTotalDebit() - (party.getBalance()));
        }
        if (partyTransaction.getTransactionType() == TransactionType.CREDIT) {
            party.setBalance(party.getBalance() + partyTransaction.getAmount());
        } else {
            party.setBalance(party.getBalance() - partyTransaction.getAmount());
        }

        if(party.getBalance() < 0){
            business.setTotalCredit(business.getTotalCredit() - (party.getBalance()));
        }else{
            business.setTotalDebit(business.getTotalDebit() + (party.getBalance()));
        }
        businessRepository.save(business);
        partyRepository.save(party);
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PartyTransaction updateTransaction(PartyTransaction partyTransaction) {


        PartyTransaction partyTransaction1 = transactionRepository.findById(partyTransaction.getId()).orElseThrow(() -> new RuntimeException("Transaction not found"));
        logger.info("Amount1 {} ", partyTransaction1.getAmount());
        Business business = partyTransaction1.getParty().getBusiness();
        Party party = partyTransaction1.getParty();
        if(party.getBalance() < 0){
            business.setTotalCredit(business.getTotalCredit() + (party.getBalance()));
        }else{
            business.setTotalDebit(business.getTotalDebit() - (party.getBalance()));
        }
        if (partyTransaction1.getTransactionType() == TransactionType.CREDIT) {
            logger.info("prev Balance1 {} ", party.getBalance());
            logger.info("Amount {} ", partyTransaction.getAmount());

            party.setBalance(party.getBalance() + partyTransaction1.getAmount() - partyTransaction.getAmount());
            logger.info("Balance1 {} ", party.getBalance());
        } else {
            logger.info("prev Balance1 {} ", party.getBalance());
            party.setBalance(party.getBalance() - partyTransaction1.getAmount() + partyTransaction.getAmount());
            logger.info("Balance11 {} ", party.getBalance());
        }

        if(party.getBalance() < 0){
            business.setTotalCredit(business.getTotalCredit() - (party.getBalance()));
        }else{
            business.setTotalDebit(business.getTotalDebit() + (party.getBalance()));
        }

        partyTransaction1.setAmount(partyTransaction.getAmount());
        partyTransaction1.setDescription(partyTransaction.getDescription());
        partyTransaction1.setTransactionDate(partyTransaction.getTransactionDate());



        if(partyTransaction1.getTransactionDate() == null){
            partyTransaction1.setTransactionDate(new Date());
        }

        businessRepository.save(business);
        partyRepository.save(party);
        return transactionRepository.save(partyTransaction1);
    }

    @Override
    public PartyTransaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + transactionId + " not found"));
    }

    private void updateBusinessAndPartyBalances(Business business, Party party, double amount, TransactionType transactionType) {
        if (party.getBalance() < 0) {
            business.setTotalCredit(business.getTotalCredit() + party.getBalance());
        } else {
            business.setTotalDebit(business.getTotalDebit() - party.getBalance());
        }

        if (transactionType == TransactionType.CREDIT) {
            party.setBalance(party.getBalance() - amount);
        } else {
            party.setBalance(party.getBalance() + amount);
        }

        if (party.getBalance() < 0) {
            business.setTotalCredit(business.getTotalCredit() - party.getBalance());
        } else {
            business.setTotalDebit(business.getTotalDebit() + party.getBalance());
        }
    }
}