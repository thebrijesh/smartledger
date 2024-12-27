package com.sml.smartledger.Repository.inventory;

import com.sml.smartledger.Model.inventory.ProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductTransactionRepository extends JpaRepository<ProductTransaction,Long> {

    public List<ProductTransaction> findAllProductTransactionByProductId(Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductTransaction pt WHERE pt.id = :transactionId")
    void deleteById(@Param("transactionId") Long id);
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductTransaction pt WHERE pt.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long id);
}
