package com.ebiz.bcube.domain.activities.repository;

import com.ebiz.bcube.domain.activities.entity.Designton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesigntonRepository extends JpaRepository<Designton, Long> {
}
