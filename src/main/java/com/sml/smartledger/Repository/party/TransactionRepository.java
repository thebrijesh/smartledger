package com.sml.smartledger.Repository.party;

import com.sml.smartledger.Model.party.PartyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<PartyTransaction,Long> {
    List<PartyTransaction> findAllByPartyId(Long partyId);
}
