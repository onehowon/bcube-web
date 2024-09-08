package com.ebiz.bcube.domain.contact_info.repository;

import com.ebiz.bcube.domain.contact_info.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
}
