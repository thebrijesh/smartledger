package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.CustomFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomFieldsRepository extends JpaRepository<CustomFields, Long> {

    List<CustomFields> findAllByBusinessId(Long businessId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomFields cf WHERE cf.id = :customFields")
    void deleteById(@Param("customFields") Long id);
}
