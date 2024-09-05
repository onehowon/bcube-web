package com.ebiz.bcube.domain.executives.repository;

import com.ebiz.bcube.domain.executives.entity.Executives;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutivesRepository extends JpaRepository<Executives, Long> {
}
