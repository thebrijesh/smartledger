package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.AdditionalCharges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalChargesRepository extends JpaRepository<AdditionalCharges, Long> {
    List<AdditionalCharges> findAllByBusinessId(Long businessId);
}
