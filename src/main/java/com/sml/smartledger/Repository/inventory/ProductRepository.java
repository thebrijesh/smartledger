package com.sml.smartledger.Repository.inventory;


import com.sml.smartledger.Model.inventory.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    public List<Product> getAllProductByBusinessId(Long businessId);
}
