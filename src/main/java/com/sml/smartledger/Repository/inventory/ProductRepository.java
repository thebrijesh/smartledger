package com.sml.smartledger.Repository.inventory;


import com.sml.smartledger.Model.inventory.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    public List<Product> getAllProductByBusinessId(Long businessId);

    @Modifying
    @Transactional
    @Query("DELETE FROM products pt WHERE pt.id = :productId")
    void deleteById(@Param("productId") Long id);

}
