package com.ebiz.bcube.domain.ob_interview.service;

import com.ebiz.bcube.global.exception.RecordNotFoundException;
import com.ebiz.bcube.domain.ob_interview.dto.ObInterviewDTO;
import com.ebiz.bcube.domain.ob_interview.entity.ObInterview;
import com.ebiz.bcube.domain.ob_interview.repository.ObInterviewRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ObInterviewService {

    private final OracleObjectStorageService objectStorageService;
    private final ObInterviewRepository repository;
    private final String bucketUrl;

    @Autowired
    public ObInterviewService(OracleObjectStorageService objectStorageService, ObInterviewRepository repository, @Value("${oracle.cloud.bucket-url}") String bucketUrl) {
        this.objectStorageService = objectStorageService;
        this.repository = repository;
        this.bucketUrl = bucketUrl;
    }

    public ObInterviewDTO createObInterview(String name, String studentId, String message, MultipartFile multipartFile, String email) throws IOException {
        String imageUrl = saveImageAndGetUrl(multipartFile);

        ObInterview obInterview = ObInterview.builder()
                .name(name)
                .studentId(studentId)
                .message(message)
                .imagePath(imageUrl)
                .email(email)
                .build();

        ObInterview savedObInterview = repository.save(obInterview);

        return convertToDto(savedObInterview);
    }

    public List<ObInterviewDTO> getAllObInterviews() {
        List<ObInterview> obInterviews = repository.findAll();
        return obInterviews.stream()
                .map(this::convertToDto)
                .toList();
    }

    public ObInterviewDTO getObInterviewById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RecordNotFoundException("인터뷰 정보를 찾을 수 없습니다. id: " + id));
    }

    public ObInterviewDTO updateObInterview(Long id, String name, String studentId, String message, MultipartFile multipartFile, String email) throws IOException {
        ObInterview existingObInterview = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("인터뷰 정보를 찾을 수 없습니다. id: " + id));

        String imageUrl = saveImageAndGetUrl(multipartFile);

        ObInterview updatedObInterview = ObInterview.builder()
                .id(existingObInterview.getId())
                .name(name)
                .studentId(studentId)
                .message(message)
                .imagePath(imageUrl)
                .email(email)
                .build();

        ObInterview savedObInterview = repository.save(updatedObInterview);

        return convertToDto(savedObInterview);
    }

    public void deleteObInterview(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RecordNotFoundException("인터뷰 정보를 찾을 수 없습니다. id: " + id);
        }
    }

    private String saveImageAndGetUrl(MultipartFile imageFile) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();

        byte[] content = imageFile.getBytes();

        objectStorageService.uploadObject(objectName, content);

        String imageUrl = String.format(
                "https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                objectStorageService.getRegion(),
                objectStorageService.getNamespace(),
                objectStorageService.getBucketName(),
                objectName
        );

        return imageUrl;
    }

    private ObInterviewDTO convertToDto(ObInterview obInterview) {
        return ObInterviewDTO.builder()
                .id(obInterview.getId())
                .name(obInterview.getName())
                .studentId(obInterview.getStudentId())
                .message(obInterview.getMessage())
                .imageUrl(obInterview.getImagePath())
                .email(obInterview.getEmail())
                .build();
    }
}