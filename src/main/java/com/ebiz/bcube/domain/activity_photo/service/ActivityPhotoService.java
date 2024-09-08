package com.ebiz.bcube.domain.activity_photo.service;

import java.util.UUID;

import com.ebiz.bcube.domain.activity_photo.dto.ActivityPhotoDTO;
import com.ebiz.bcube.domain.activity_photo.entity.ActivityPhoto;
import com.ebiz.bcube.domain.activity_photo.repository.ActivityPhotoRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ActivityPhotoService {

    private final OracleObjectStorageService objectStorageService;
    private final ActivityPhotoRepository repository;
    private final String bucketUrl;

    @Autowired
    public ActivityPhotoService(OracleObjectStorageService objectStorageService, ActivityPhotoRepository repository, @Value("${oracle.cloud.bucket-url}") String bucketUrl) {
        this.objectStorageService = objectStorageService;
        this.repository = repository;
        this.bucketUrl = bucketUrl;
    }

    public ActivityPhotoDTO createActivityPhoto(String description, MultipartFile multipartFile) throws IOException {
        String imageUrl = saveImageAndGetUrl(multipartFile);

        ActivityPhoto activityPhoto = ActivityPhoto.builder()
                .description(description)
                .imagePath(imageUrl)
                .build();

        ActivityPhoto savedActivityPhoto = repository.save(activityPhoto);

        return convertToDto(savedActivityPhoto);
    }

    public List<ActivityPhotoDTO> getAllActivityPhotos() {
        List<ActivityPhoto> activityPhotos = repository.findAll();
        return activityPhotos.stream()
                .map(this::convertToDto)
                .toList();
    }

    public ActivityPhotoDTO getActivityPhotoById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("사진 id를 찾을 수 없습니다. " + id));
    }

    public ActivityPhotoDTO updateActivityPhoto(Long id, String description, MultipartFile imageFile) throws IOException {
        ActivityPhoto existingActivityPhoto = repository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("id를 찾을 수 없습니다: " + id));

        String imageUrl = saveImageAndGetUrl(imageFile);

        ActivityPhoto updatedActivityPhoto = ActivityPhoto.builder()
                .id(existingActivityPhoto.getId())
                .description(description)
                .imagePath(imageUrl)
                .build();

        ActivityPhoto savedActivityPhoto = repository.save(updatedActivityPhoto);

        return convertToDto(savedActivityPhoto);
    }

    public void deleteActivityPhoto(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new FileNotFoundException("id를 찾을 수 없습니다: " + id);
        }
    }

    private String saveImageAndGetUrl(MultipartFile imageFile) throws IOException {
        // 파일의 고유한 객체 이름 생성
        String objectName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();

        // MultipartFile을 바이트 배열로 변환
        byte[] content = imageFile.getBytes();

        // OCI Object Storage에 이미지 업로드
        objectStorageService.uploadObject(objectName, content);

        // 업로드된 이미지의 URL 생성
        String imageUrl = String.format(
                "https://%s.compat.objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                objectStorageService.getNamespace(),
                objectStorageService.getRegion(),
                objectStorageService.getBucketName(),
                objectStorageService.getBucketName(),
                objectName
        );

        return imageUrl;
    }

    private ActivityPhotoDTO convertToDto(ActivityPhoto activityPhoto) {
        return ActivityPhotoDTO.builder()
                .id(activityPhoto.getId())
                .description(activityPhoto.getDescription())
                .imageUrl(activityPhoto.getImagePath())
                .build();
    }
}