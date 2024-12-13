package com.sml.smartledger.Services.implementetion.party;

import com.sml.smartledger.Model.bill.Bill;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.party.PartyRepository;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyServiceImpl implements PartyService {
    @Autowired
    PartyRepository partyRepository;
    @Autowired
    BusinessRepository businessRepository;

    @Override
    public Party createParty(Party party) {
        Optional<Business> businessOptional = businessRepository.findById(party.getBusiness().getId());
        if (businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        Party savedParty = partyRepository.save(party);
        business.getParties().add(savedParty);
        businessRepository.save(business);
        return savedParty;
    }


    @Transactional
    public void deleteParty(Long id) {
        partyRepository.deletePartyTransactionsByPartyId(id);
        partyRepository.deleteById(id);
    }

    @Override
    public List<Party> getAllParty(Long businessId) {
        return partyRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Party> getCustomerParty(@NonNull Long id) {
        return partyRepository.findAllByBusinessIdAndPartyType(id, PartyType.CUSTOMER);
    }

    @Override
    public Party getPartyById(Long partyId) {
        return partyRepository.findById(partyId).orElseThrow(() -> new RuntimeException("Party not found"));
    }

    @Override
    public Party updateParty(Party party) {
        return partyRepository.save(party);
    }

    @Override
    public List<Party> getSupplierParty(@NonNull Long id) {
        return partyRepository.findAllByBusinessIdAndPartyType(id, PartyType.SUPPLIER);
    }
}
