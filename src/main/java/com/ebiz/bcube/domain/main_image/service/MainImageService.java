package com.ebiz.bcube.domain.main_image.service;

import com.ebiz.bcube.domain.main_image.dto.MainImageDTO;
import com.ebiz.bcube.domain.main_image.entity.MainImage;
import com.ebiz.bcube.domain.main_image.repository.MainImageRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MainImageService {

    private final OracleObjectStorageService objectStorageService;
    private final MainImageRepository repository;
    private final String bucketUrl;

    @Autowired
    public MainImageService(OracleObjectStorageService objectStorageService, MainImageRepository repository, @Value("${oracle.cloud.bucket-url}") String bucketUrl) {
        this.objectStorageService = objectStorageService;
        this.repository = repository;
        this.bucketUrl = bucketUrl;
    }

    // Main Image 생성
    public MainImageDTO createMainImage(MultipartFile multipartFile) throws IOException {
        String imageUrl = saveImageAndGetUrl(multipartFile);

        // 변수 이름을 다른 것으로 변경
        MainImage newMainImage = MainImage.builder()
                .imagePath(imageUrl)
                .build();

        MainImage savedMainImage = repository.save(newMainImage);

        return convertToDto(savedMainImage);
    }

    // 모든 Main Image 조회
    public List<MainImageDTO> getAllMainImages() {
        List<MainImage> mainImages = repository.findAll();
        return mainImages.stream()
                .map(this::convertToDto)
                .toList();
    }

    // ID로 Main Image 조회
    public MainImageDTO getMainImageById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("메인 이미지를 찾을 수 없습니다. ID: " + id));
    }

    // Main Image 업데이트
    public MainImageDTO updateMainImage(Long id, MultipartFile imageFile) throws IOException {
        MainImage existingMainImage = repository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("메인 이미지를 찾을 수 없습니다. ID: " + id));

        String imageUrl = saveImageAndGetUrl(imageFile);

        MainImage updatedMainImage = MainImage.builder()
                .id(existingMainImage.getId())
                .imagePath(imageUrl)
                .build();

        MainImage savedMainImage = repository.save(updatedMainImage);

        return convertToDto(savedMainImage);
    }

    // Main Image 삭제
    public void deleteMainImage(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new FileNotFoundException("메인 이미지를 찾을 수 없습니다. ID: " + id);
        }
    }

    // Oracle Object Storage에 이미지 저장 및 URL 반환
    private String saveImageAndGetUrl(MultipartFile imageFile) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();
        byte[] content = imageFile.getBytes();

        objectStorageService.uploadObject(objectName, content);

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

    // Entity -> DTO 변환
    private MainImageDTO convertToDto(MainImage mainImage) {
        return MainImageDTO.builder()
                .id(mainImage.getId())
                .imageUrl(mainImage.getImagePath())
                .build();
    }
}