package com.sml.smartledger.Repository.inventory;


import com.sml.smartledger.Model.bill.BillProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<BillProduct,Long> {
    public List<BillProduct> getAllProductByBusinessId(Long businessId);
}
