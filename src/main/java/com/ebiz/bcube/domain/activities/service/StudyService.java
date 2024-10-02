package com.ebiz.bcube.domain.activities.service;

import com.ebiz.bcube.domain.activities.dto.StudyDTO;
import com.ebiz.bcube.domain.activities.entity.Study;
import com.ebiz.bcube.domain.activities.repository.StudyRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class StudyService {

    private final OracleObjectStorageService objectStorageService;
    private final StudyRepository studyRepository;

    @Autowired
    public StudyService(OracleObjectStorageService objectStorageService, StudyRepository studyRepository) {
        this.objectStorageService = objectStorageService;
        this.studyRepository = studyRepository;
    }

    public StudyDTO createStudy(StudyDTO dto, MultipartFile imageFile) throws IOException {
        String imageUrl = saveFileAndGetUrl(imageFile);

        Study study = Study.builder()
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .url(dto.getUrl())
                .build();

        Study savedStudy = studyRepository.save(study);

        return convertToDto(savedStudy);
    }

    public List<StudyDTO> getAllStudy() {
        List<Study> studyList = studyRepository.findAll();
        return studyList.stream().map(this::convertToDto).toList();
    }

    public StudyDTO getStudyById(Long id) {
        return studyRepository.findById(id).map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("해당 스터디 활동을 찾을 수 없음: " + id));
    }

    public StudyDTO updateStudy(Long id, StudyDTO dto, MultipartFile imageFile) throws IOException {
        Study existingStudy = studyRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("해당 스터디 활동을 찾을 수 없음: " + id));

        String imageUrl = saveFileAndGetUrl(imageFile);

        Study updatedStudy = Study.builder()
                .id(existingStudy.getId())
                .year(dto.getYear())
                .title(dto.getTitle())
                .participants(dto.getParticipants())
                .imagePath(imageUrl)
                .url(dto.getUrl())
                .build();

        Study savedStudy = studyRepository.save(updatedStudy);

        return convertToDto(savedStudy);
    }

    public void deleteStudy(Long id) {
        if (studyRepository.existsById(id)) {
            studyRepository.deleteById(id);
        } else {
            throw new FileNotFoundException("해당 스터디 활동을 찾을 수 없음: " + id);
        }
    }

    private String saveFileAndGetUrl(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        byte[] content = file.getBytes();
        objectStorageService.uploadObject(objectName, content);
        return objectStorageService.getFileUrl(objectName);
    }

    private StudyDTO convertToDto(Study study) {
        return StudyDTO.builder()
                .id(study.getId())
                .year(study.getYear())
                .title(study.getTitle())
                .participants(study.getParticipants())
                .imageUrl(study.getImagePath())
                .url(study.getUrl())
                .build();
    }
}