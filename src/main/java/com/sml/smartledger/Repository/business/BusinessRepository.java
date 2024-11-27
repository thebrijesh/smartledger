package com.sml.smartledger.Repository.business;


import com.sml.smartledger.Model.business.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business,Long> {

    List<Business> findByUserId(String userid);
}
