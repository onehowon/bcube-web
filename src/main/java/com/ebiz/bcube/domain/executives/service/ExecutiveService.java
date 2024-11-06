package com.ebiz.bcube.domain.executives.service;

import com.ebiz.bcube.domain.executives.dto.ExecutivesDTO;
import com.ebiz.bcube.domain.executives.entity.Executives;
import com.ebiz.bcube.domain.executives.repository.ExecutivesRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExecutiveService {

    private final OracleObjectStorageService objectStorageService;
    private final ExecutivesRepository repository;
    private final String bucketUrl;

    @Autowired
    public ExecutiveService(OracleObjectStorageService objectStorageService, ExecutivesRepository repository, @Value("${oracle.cloud.bucket-url}") String bucketUrl) {
        this.objectStorageService = objectStorageService;
        this.repository = repository;
        this.bucketUrl = bucketUrl;
    }

    public ExecutivesDTO createExecutive(ExecutivesDTO executivesDTO, MultipartFile multipartFile) throws IOException {
        String imageUrl = saveImageAndGetUrl(multipartFile);

        Executives executive = Executives.builder()
                .memberName(executivesDTO.getMemberName())
                .role(executivesDTO.getRole())
                .studentId(executivesDTO.getStudentId())
                .basicInfo(executivesDTO.getBasicInfo())
                .imagePath(imageUrl)
                .build();

        Executives savedExecutive = repository.save(executive);

        return convertToDto(savedExecutive);
    }

    public List<ExecutivesDTO> getAllExecutives() {
        List<Executives> executives = repository.findAll();
        return executives.stream()
                .map(this::convertToDto)
                .toList();
    }

    public ExecutivesDTO getExecutiveById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("회장단 id를 찾을 수 없습니다. " + id));
    }


    public ExecutivesDTO updateExecutive(Long id, ExecutivesDTO dto, MultipartFile imageFile) throws IOException {
        Executives existingExecutive = repository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("id를 찾을 수 없습니다: " + id));

        String imageUrl = saveImageAndGetUrl(imageFile);

        Executives updatedExecutive = Executives.builder()
                .id(existingExecutive.getId())
                .memberName(dto.getMemberName())
                .role(dto.getRole())
                .studentId(dto.getStudentId())
                .basicInfo(dto.getBasicInfo())
                .imagePath(imageUrl)
                .build();

        Executives savedExecutive = repository.save(updatedExecutive);

        return convertToDto(savedExecutive);
    }

    public void deleteExecutive(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new FileNotFoundException("id를 찾을 수 없습니다: " + id);
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

    private ExecutivesDTO convertToDto(Executives executive) {
        return ExecutivesDTO.builder()
                .id(executive.getId())
                .memberName(executive.getMemberName())
                .role(executive.getRole())
                .studentId(executive.getStudentId())
                .basicInfo(executive.getBasicInfo())
                .imageUrl(executive.getImagePath())
                .build();
    }
}
