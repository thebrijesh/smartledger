package com.sml.smartledger.Repository.party;

import com.sml.smartledger.Model.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party,Long> {
  public List<Party> findByBusinessId(Long businessId);
}
