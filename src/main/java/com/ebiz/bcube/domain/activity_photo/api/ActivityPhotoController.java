package com.ebiz.bcube.domain.activity_photo.api;

import com.ebiz.bcube.domain.activity_photo.dto.ActivityPhotoDTO;
import com.ebiz.bcube.domain.activity_photo.service.ActivityPhotoService;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/activity-photos")
public class ActivityPhotoController {

    private final ActivityPhotoService activityPhotoService;
    private final String bucketUrl;

    public ActivityPhotoController(ActivityPhotoService activityPhotoService, @Value("${oracle.cloud.bucket-url}") String bucketUrl) {
        this.activityPhotoService = activityPhotoService;
        this.bucketUrl = bucketUrl;
    }

    @PostMapping
    public ResponseEntity<ActivityPhotoDTO> createActivityPhoto(
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam("date") LocalDate date) throws IOException {

        ActivityPhotoDTO createdActivityPhoto = activityPhotoService.createActivityPhoto(description, multipartFile, date);
        return new ResponseEntity<>(createdActivityPhoto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityPhotoDTO> getActivityPhotoById(@PathVariable Long id) {
        ActivityPhotoDTO activityPhoto = activityPhotoService.getActivityPhotoById(id);
        return ResponseEntity.ok(activityPhoto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityPhotoDTO> updateActivityPhoto(
            @PathVariable Long id,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        ActivityPhotoDTO updatedActivityPhoto = activityPhotoService.updateActivityPhoto(id, description, multipartFile);
        return ResponseEntity.ok(updatedActivityPhoto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityPhoto(@PathVariable Long id) {
        activityPhotoService.deleteActivityPhoto(id);
        return ResponseEntity.noContent().build();
    }
}
