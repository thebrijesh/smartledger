package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.CustomFields;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomFieldsRepository extends JpaRepository<CustomFields, Long> {

    List<CustomFields> findAllByBusinessId(Long businessId);
}
