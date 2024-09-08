package com.ebiz.bcube.domain.activity_photo.repository;

import com.ebiz.bcube.domain.activity_photo.entity.ActivityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityPhotoRepository extends JpaRepository<ActivityPhoto, Long> {
}
