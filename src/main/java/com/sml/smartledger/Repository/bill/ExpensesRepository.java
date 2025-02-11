package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.Expenses;
import com.sml.smartledger.Model.bill.ExpensesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses,Long> {

    List<Expenses> findByBusinessId(Long businessId);
    boolean existsByExpensesCategory(ExpensesCategory category);

    @Modifying
    @Transactional
    @Query("DELETE FROM expense ex WHERE ex.id = :expensesId")
    void deleteByExpensesId(@Param("expensesId") Long id);

}
