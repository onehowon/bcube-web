package com.ebiz.bcube.domain.activities.service;

import com.ebiz.bcube.domain.activities.dto.EtcDTO;
import com.ebiz.bcube.domain.activities.entity.Etc;
import com.ebiz.bcube.domain.activities.repository.EtcRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class EtcService {

    private final OracleObjectStorageService objectStorageService;
    private final EtcRepository etcRepository;

    @Autowired
    public EtcService(OracleObjectStorageService objectStorageService, EtcRepository etcRepository) {
        this.objectStorageService = objectStorageService;
        this.etcRepository = etcRepository;
    }

    public EtcDTO createEtc(EtcDTO dto, MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = pdfFile != null ? saveFileAndGetUrl(pdfFile) : null;

        Etc etc = Etc.builder()
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .url(dto.getUrl())
                .pdfUrl(pdfUrl)
                .build();

        Etc savedEtc = etcRepository.save(etc);

        return convertToDto(savedEtc);
    }

    public List<EtcDTO> getAllEtc() {
        List<Etc> etcList = etcRepository.findAll();
        return etcList.stream().map(this::convertToDto).toList();
    }

    public EtcDTO getEtcById(Long id) {
        return etcRepository.findById(id).map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("해당 기타활동을 찾을 수 없음: " + id));
    }

    public EtcDTO updateEtc(Long id, EtcDTO dto, MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        Etc existingEtc = etcRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("해당 기타활동을 찾을 수 없음: " + id));

        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = pdfFile != null ? saveFileAndGetUrl(pdfFile) : existingEtc.getPdfUrl();

        Etc updatedEtc = Etc.builder()
                .id(existingEtc.getId())
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .url(dto.getUrl())
                .pdfUrl(pdfUrl)
                .build();

        Etc savedEtc = etcRepository.save(updatedEtc);

        return convertToDto(savedEtc);
    }

    public void deleteEtc(Long id) {
        if (etcRepository.existsById(id)) {
            etcRepository.deleteById(id);
        } else {
            throw new FileNotFoundException("해당 기타활동을 찾을 수 없음: " + id);
        }
    }

    private String saveFileAndGetUrl(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        byte[] content = file.getBytes();
        objectStorageService.uploadObject(objectName, content);
        return objectStorageService.getFileUrl(objectName);
    }

    private EtcDTO convertToDto(Etc etc) {
        return EtcDTO.builder()
                .id(etc.getId())
                .year(etc.getYear())
                .title(etc.getTitle())
                .participants(etc.getParticipants())
                .imageUrl(etc.getImagePath())
                .url(etc.getUrl())
                .pdfUrl(etc.getPdfUrl() != null && !etc.getPdfUrl().isEmpty() ? etc.getPdfUrl() : null)  // 빈 문자열이면 null로 설정
                .build();
    }
}
