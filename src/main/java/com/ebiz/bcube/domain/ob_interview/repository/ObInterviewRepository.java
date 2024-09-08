package com.ebiz.bcube.domain.ob_interview.repository;

import com.ebiz.bcube.domain.ob_interview.entity.ObInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObInterviewRepository extends JpaRepository<ObInterview, Long> {
}
