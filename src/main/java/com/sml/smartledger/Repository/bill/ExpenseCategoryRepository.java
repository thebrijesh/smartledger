package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.ExpensesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpensesCategory,Long> {

    List<ExpensesCategory> findByBusinessId(Long businessId);
    @Modifying
    @Transactional
    @Query("DELETE FROM ExpensesCategory ex WHERE ex.id = :expensesCategoryId")
    void deleteByExpensesCategoryId(@Param("expensesCategoryId") Long id);
    @Modifying
    @Transactional
    @Query("DELETE FROM expense ex WHERE ex.expensesCategory.id = :expensesCategoriesId")
    void deleteExpensesByExpensesCategoryId(@Param("expensesCategoriesId") Long id);
    boolean existsByName(String name);


}
