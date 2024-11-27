package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.ExpansesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpanceCatagoryRepository extends JpaRepository<ExpansesCategory,Long> {

    List<ExpansesCategory> findByBusinessId(Long businessId);
}
