package com.sml.smartledger.Repository.inventory;


import com.sml.smartledger.Model.inventory.ServiceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTransactionRepository extends JpaRepository<ServiceTransaction, Long> {
    List<ServiceTransaction> findAllServiceTransactionByBillServiceId(Long billServiceId);
}
