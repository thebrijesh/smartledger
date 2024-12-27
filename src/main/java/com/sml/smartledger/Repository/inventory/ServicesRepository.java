package com.sml.smartledger.Repository.inventory;




import com.sml.smartledger.Model.inventory.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ServicesRepository extends JpaRepository<Service, Long> {
    List<Service> findAllServiceByBusinessId(Long businessId);

    @Modifying
    @Transactional
    @Query("DELETE FROM services s WHERE s.id = :serviceId")
    void deleteById(@Param("serviceId") Long id);
}
