package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.ExpansesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpanceItemRepository  extends JpaRepository<ExpansesItem,Long> {
    List<ExpansesItem> findByBusinessId(Long businessId);
}
