package com.sml.smartledger.Repository.bill;


import com.sml.smartledger.Model.bill.BillService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillServiceRepository extends JpaRepository<BillService,Long> {
    public List<BillService> findAllServiceByBusinessId(Long businessId);
}
