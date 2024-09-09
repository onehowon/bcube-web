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
        // 이미지와 PDF 파일을 각각 업로드하여 URL을 가져옴
        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = saveFileAndGetUrl(pdfFile); // pdfFile도 같은 방식으로 처리

        // Designton 엔티티 빌더를 통해 저장할 객체 생성
        Designton designton = Designton.builder()
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .pdfFile(pdfUrl)
                .build();

        // 데이터베이스에 저장
        Designton savedDesignton = designtonRepository.save(designton);

        // DTO 변환 및 반환
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

        // 이미지와 PDF 파일을 각각 업로드하여 URL을 업데이트
        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = saveFileAndGetUrl(pdfFile);

        // 기존 데이터를 업데이트
        Designton updatedDesignton = Designton.builder()
                .id(existingDesignton.getId())
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .pdfFile(pdfUrl)
                .build();

        // 데이터베이스에 저장
        Designton savedDesignton = designtonRepository.save(updatedDesignton);

        // DTO 변환 및 반환
        return convertToDto(savedDesignton);
    }

    public void deleteDesignton(Long id) {
        if (designtonRepository.existsById(id)) {
            designtonRepository.deleteById(id);
        } else {
            throw new FileNotFoundException("Designton id not found: " + id);
        }
    }

    // 파일을 저장하고 URL을 반환하는 메서드
    private String saveFileAndGetUrl(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        byte[] content = file.getBytes();
        // 파일을 업로드하고 URL을 반환
        objectStorageService.uploadObject(objectName, content);
        return objectStorageService.getFileUrl(objectName);
    }

    // 엔티티를 DTO로 변환하는 메서드
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
