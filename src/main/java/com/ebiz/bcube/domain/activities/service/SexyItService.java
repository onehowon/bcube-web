package com.ebiz.bcube.domain.activities.service;

import com.ebiz.bcube.domain.activities.dto.SexyItDTO;
import com.ebiz.bcube.domain.activities.entity.SexyIt;
import com.ebiz.bcube.domain.activities.repository.SexyItRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class SexyItService {

    private final OracleObjectStorageService objectStorageService;
    private final SexyItRepository sexyItRepository;

    @Autowired
    public SexyItService(OracleObjectStorageService objectStorageService, SexyItRepository sexyItRepository) {
        this.objectStorageService = objectStorageService;
        this.sexyItRepository = sexyItRepository;
    }

    public SexyItDTO createSexyIt(SexyItDTO dto, MultipartFile imageFile) throws IOException {
        String imageUrl = saveFileAndGetUrl(imageFile);

        SexyIt sexyIt = SexyIt.builder()
                .date(dto.getDate())
                .title(dto.getTitle())
                .imagePath(imageUrl)
                .url(dto.getUrl())
                .build();

        SexyIt savedSexyIt = sexyItRepository.save(sexyIt);

        return convertToDto(savedSexyIt);
    }

    public List<SexyItDTO> getAllSexyIt() {
        List<SexyIt> sexyItList = sexyItRepository.findAll();
        return sexyItList.stream().map(this::convertToDto).toList();
    }

    public SexyItDTO getSexyItById(Long id) {
        return sexyItRepository.findById(id).map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("해당 ID의 섹시한 IT를 찾을 수 없음: " + id));
    }

    public SexyItDTO updateSexyIt(Long id, SexyItDTO dto, MultipartFile imageFile) throws IOException {
        SexyIt existingSexyIt = sexyItRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("해당 ID의 섹시한 IT를 찾을 수 없음: " + id));

        String imageUrl = saveFileAndGetUrl(imageFile);

        SexyIt updatedSexyIt = SexyIt.builder()
                .id(existingSexyIt.getId())
                .date(dto.getDate())
                .title(dto.getTitle())
                .imagePath(imageUrl)
                .url(dto.getUrl())
                .build();

        SexyIt savedSexyIt = sexyItRepository.save(updatedSexyIt);

        return convertToDto(savedSexyIt);
    }

    public void deleteSexyIt(Long id) {
        if (sexyItRepository.existsById(id)) {
            sexyItRepository.deleteById(id);
        } else {
            throw new FileNotFoundException("해당 ID의 섹시한 IT를 찾을 수 없음: " + id);
        }
    }

    private String saveFileAndGetUrl(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        byte[] content = file.getBytes();
        objectStorageService.uploadObject(objectName, content);
        return objectStorageService.getFileUrl(objectName);
    }

    private SexyItDTO convertToDto(SexyIt sexyIt) {
        return SexyItDTO.builder()
                .id(sexyIt.getId())
                .date(sexyIt.getDate())
                .title(sexyIt.getTitle())
                .imageUrl(sexyIt.getImagePath())
                .url(sexyIt.getUrl())
                .build();
    }
}