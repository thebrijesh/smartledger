package com.sml.smartledger.Repository.staff;

import com.sml.smartledger.Model.staff.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<StaffMember,Long> {
    List<StaffMember> findAllByBusinessId(Long businessId);
}
