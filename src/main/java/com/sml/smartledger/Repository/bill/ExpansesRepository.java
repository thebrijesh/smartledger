package com.sml.smartledger.Repository.bill;

import com.sml.smartledger.Model.bill.Expanses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpansesRepository extends JpaRepository<Expanses,Long> {

    List<Expanses> findByBusinessId(Long businessId);
}
