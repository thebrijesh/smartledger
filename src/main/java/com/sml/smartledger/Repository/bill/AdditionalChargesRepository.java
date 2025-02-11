package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.AdditionalCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdditionalChargesRepository extends JpaRepository<AdditionalCharges, Long> {
    List<AdditionalCharges> findAllByBusinessId(Long businessId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AdditionalCharges ac WHERE ac.id = :additionalCharges")
    void deleteById(@Param("additionalCharges") Long id);
}
