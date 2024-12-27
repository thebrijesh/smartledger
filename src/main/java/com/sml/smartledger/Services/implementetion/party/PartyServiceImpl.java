package com.sml.smartledger.Services.implementetion.party;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Repository.party.PartyRepository;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.sml.smartledger.Helper.Helper.generateShortCode;

@Service
public class PartyServiceImpl implements PartyService {

    PartyRepository partyRepository;
    BusinessRepository businessRepository;

    @Autowired
    public PartyServiceImpl(PartyRepository partyRepository, BusinessRepository businessRepository) {
        this.partyRepository = partyRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    public Party createParty(Party party) {
        Optional<Business> businessOptional = businessRepository.findById(party.getBusiness().getId());
        if (businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();
         party.setShortCode(generateShortCode());
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
    public List<Party> getAllParties(Long businessId) {
        return partyRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Party> getCustomerParties(@NonNull Long id) {
        return partyRepository.findAllByBusinessIdAndPartyType(id, PartyType.CUSTOMER);
    }

    @Override
//    @Cacheable(value = "party", key = "#partyId")
    public Party getPartyById(Long partyId) {
        return partyRepository.findById(partyId).orElseThrow(() -> new RuntimeException("Party not found"));
    }

    @Override
    public void updateParty(Party party) {
        partyRepository.save(party);
    }

    @Override
    public List<Party> getSupplierParties(@NonNull Long id) {
        return partyRepository.findAllByBusinessIdAndPartyType(id, PartyType.SUPPLIER);
    }

    @Override
    public Party getPartyByShortCode(String shortCode) {
        return partyRepository.findByShortCode(shortCode);
    }

    @Override
    public List<Party> getDueParties(@NonNull Long id) {
        return partyRepository.findAllByBusinessIdAndDueDateNotNull(id);
    }

}
