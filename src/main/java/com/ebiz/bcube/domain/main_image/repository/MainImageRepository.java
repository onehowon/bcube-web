package com.ebiz.bcube.domain.main_image.repository;

import com.ebiz.bcube.domain.main_image.entity.MainImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainImageRepository extends JpaRepository<MainImage, Long> {
}
