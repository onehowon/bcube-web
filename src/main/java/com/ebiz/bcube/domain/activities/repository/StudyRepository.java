package com.ebiz.bcube.domain.activities.repository;

import com.ebiz.bcube.domain.activities.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
}
