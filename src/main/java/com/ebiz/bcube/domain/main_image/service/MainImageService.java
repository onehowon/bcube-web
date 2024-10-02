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

    public MainImageDTO createMainImage(MultipartFile multipartFile) throws IOException {
        String imageUrl = saveImageAndGetUrl(multipartFile);

        MainImage newMainImage = MainImage.builder()
                .imagePath(imageUrl)
                .build();

        MainImage savedMainImage = repository.save(newMainImage);

        return convertToDto(savedMainImage);
    }

    public List<MainImageDTO> getAllMainImages() {
        List<MainImage> mainImages = repository.findAll();
        return mainImages.stream()
                .map(this::convertToDto)
                .toList();
    }

    public MainImageDTO getMainImageById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("메인 이미지를 찾을 수 없습니다. ID: " + id));
    }

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

    public void deleteMainImage(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new FileNotFoundException("메인 이미지를 찾을 수 없습니다. ID: " + id);
        }
    }

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

    private MainImageDTO convertToDto(MainImage mainImage) {
        return MainImageDTO.builder()
                .id(mainImage.getId())
                .imageUrl(mainImage.getImagePath())
                .build();
    }
}