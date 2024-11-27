package com.sml.smartledger.Services.implementetion.bill;


import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.Expanses;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Repository.bill.ExpansesRepository;
import com.sml.smartledger.Repository.business.BusinessRepository;
import com.sml.smartledger.Services.interfaces.bill.ExpansesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpansesServiceImpl implements ExpansesService {
    @Autowired
    ExpansesRepository expansesRepository;
    @Autowired
    BusinessRepository businessRepository;
    @Override
    public List<Expanses> getAllExpanses(Long businessId) {
        return expansesRepository.findByBusinessId(businessId);
    }

    @Override
    public Expanses createExpanses(Expanses expanses) {
        Optional<Business> businessOptional = businessRepository.findById(expanses.getBusiness().getId());
        if(businessOptional.isEmpty()) throw new RuntimeException("Business not found");
        Business business = businessOptional.get();

        Expanses savedExpanse = expansesRepository.save(expanses);
        business.getExpansesList().add(savedExpanse);
        businessRepository.save(business);
        return savedExpanse;
    }

    @Override
    public void deleteExpanses(Long expansesId) {
         expansesRepository.deleteById(expansesId);
    }
}
