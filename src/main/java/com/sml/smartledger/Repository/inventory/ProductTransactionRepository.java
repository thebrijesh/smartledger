package com.sml.smartledger.Repository.inventory;

import com.sml.smartledger.Model.inventory.ProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTransactionRepository extends JpaRepository<ProductTransaction,Long> {

    public List<ProductTransaction> findAllProductTransactionByBillProductId(Long productId);
}
