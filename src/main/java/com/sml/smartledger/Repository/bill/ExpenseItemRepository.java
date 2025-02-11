package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.ExpensesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExpenseItemRepository  extends JpaRepository<ExpensesItem,Long> {
    List<ExpensesItem> findByBusinessId(Long businessId);

}
