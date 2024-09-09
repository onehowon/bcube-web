package com.ebiz.bcube.domain.activities.repository;

import com.ebiz.bcube.domain.activities.entity.Etc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtcRepository extends JpaRepository<Etc, Long> {
}
