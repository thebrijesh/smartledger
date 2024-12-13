package com.sml.smartledger.Repository.party;

import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Repository
public interface TransactionRepository extends JpaRepository<PartyTransaction,Long> {
    List<PartyTransaction> findAllByPartyId(Long partyId);
    Optional<PartyTransaction> findById(Long id);
    @Modifying
    @Transactional
    @Query("DELETE FROM PartyTransaction pt WHERE pt.id = :partyId")
    void deleteById(@Param("partyId") Long id);


    List<PartyTransaction> findAllByPartyIn(List<Party> partyIds);
}
