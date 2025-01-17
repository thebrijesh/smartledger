package com.sml.smartledger.Repository.bill;


import com.sml.smartledger.Model.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllBillByBusinessId(Long businessId);
    @Query("SELECT b FROM bill b WHERE b.billType = 0 AND b.business.id = ?1")
    List<Bill> findAllSaleBillByBusinessId(Long businessId);

    @Query("SELECT b FROM bill b WHERE b.billType = 1 AND b.business.id = ?1")
    List<Bill> findAllPurchaseBillByBusinessId(Long businessId);

    @Modifying
    @Transactional
    @Query("DELETE FROM bill b WHERE b.id = :billId")
    void deleteById(@Param("billId") Long id);
}
