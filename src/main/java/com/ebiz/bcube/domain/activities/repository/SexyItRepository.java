package com.ebiz.bcube.domain.activities.repository;

import com.ebiz.bcube.domain.activities.entity.SexyIt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SexyItRepository extends JpaRepository<SexyIt, Long> {
}
