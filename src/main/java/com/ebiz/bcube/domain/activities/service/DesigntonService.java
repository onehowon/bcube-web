package com.ebiz.bcube.domain.activities.service;

import com.ebiz.bcube.domain.activities.dto.DesigntonDTO;
import com.ebiz.bcube.domain.activities.entity.Designton;
import com.ebiz.bcube.domain.activities.repository.DesigntonRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DesigntonService {

    private final OracleObjectStorageService objectStorageService;
    private final DesigntonRepository designtonRepository;

    @Autowired
    public DesigntonService(OracleObjectStorageService objectStorageService, DesigntonRepository repository) {
        this.objectStorageService = objectStorageService;
        this.designtonRepository = repository;
    }

    public DesigntonDTO createDesignton(DesigntonDTO dto, MultipartFile imageFile, MultipartFile pdfFile) throws IOException {

        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = saveFileAndGetUrl(pdfFile);

        Designton designton = Designton.builder()
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .pdfFile(pdfUrl)
                .build();

        Designton savedDesignton = designtonRepository.save(designton);

        return convertToDto(savedDesignton);
    }

    public List<DesigntonDTO> getAllDesignton() {
        List<Designton> designtonList = designtonRepository.findAll();
        return designtonList.stream().map(this::convertToDto).toList();
    }

    public DesigntonDTO getDesigntonById(Long id) {
        return designtonRepository.findById(id).map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("Designton id not found: " + id));
    }

    public DesigntonDTO updateDesignton(Long id, DesigntonDTO dto, MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        Designton existingDesignton = designtonRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("Designton id not found: " + id));

        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = saveFileAndGetUrl(pdfFile);

        Designton updatedDesignton = Designton.builder()
                .id(existingDesignton.getId())
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .pdfFile(pdfUrl)
                .build();

        Designton savedDesignton = designtonRepository.save(updatedDesignton);

        return convertToDto(savedDesignton);
    }

    public void deleteDesignton(Long id) {
        if (designtonRepository.existsById(id)) {
            designtonRepository.deleteById(id);
        } else {
            throw new FileNotFoundException("Designton id not found: " + id);
        }
    }

    private String saveFileAndGetUrl(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        byte[] content = file.getBytes();

        objectStorageService.uploadObject(objectName, content);
        return objectStorageService.getFileUrl(objectName);
    }

    private DesigntonDTO convertToDto(Designton designton) {
        return DesigntonDTO.builder()
                .id(designton.getId())
                .year(designton.getYear())
                .title(designton.getTitle())
                .participants(designton.getParticipants())
                .imageUrl(designton.getImagePath())
                .pdfUrl(designton.getPdfFile())
                .build();
    }
}
