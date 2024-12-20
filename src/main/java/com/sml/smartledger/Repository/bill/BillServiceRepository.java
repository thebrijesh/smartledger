package com.sml.smartledger.Repository.bill;


import com.sml.smartledger.Model.inventory.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillServiceRepository extends JpaRepository<Service,Long> {
    public List<Service> findAllServiceByBusinessId(Long businessId);
}
