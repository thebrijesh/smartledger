package com.sml.smartledger.Repository.party;

import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.PartyType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    public List<Party> findByBusinessId(Long businessId);

    List<Party> findAllByBusinessIdAndPartyType(Long businessId, PartyType partyType);


    @Modifying
    @Transactional
    @Query("DELETE FROM PartyTransaction pt WHERE pt.party.id = :parties_id")
    void deletePartyTransactionsByPartyId(@Param("parties_id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM party p WHERE p.id = :parties_id")
    void deleteById(@Param("parties_id") Long id);

    Party findByShortCode(String shortCode);

    //get list of customers who have due date not null
    List<Party> findAllByBusinessIdAndDueDateNotNull(Long businessId);

    Party findByMobile(String mobileNumber);
}
