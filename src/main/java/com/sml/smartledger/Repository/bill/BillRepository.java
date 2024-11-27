package com.sml.smartledger.Repository.bill;


import com.sml.smartledger.Model.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllBillByBusinessId(Long businessId);
}
