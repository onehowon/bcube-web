package com.ebiz.bcube.domain.main_activity.service;

import com.ebiz.bcube.domain.main_activity.dto.MainActivityDTO;
import com.ebiz.bcube.domain.main_activity.entity.MainActivity;
import com.ebiz.bcube.domain.main_activity.repository.MainActivityRepository;
import com.ebiz.bcube.global.application.OracleObjectStorageService;
import com.ebiz.bcube.global.exception.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MainActivityService {

    private final OracleObjectStorageService objectStorageService;
    private final MainActivityRepository mainActivityRepository;

    @Autowired
    public MainActivityService(OracleObjectStorageService objectStorageService, MainActivityRepository mainActivityRepository) {
        this.objectStorageService = objectStorageService;
        this.mainActivityRepository = mainActivityRepository;
    }

    public MainActivityDTO createMainActivity(MainActivityDTO dto, MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = saveFileAndGetUrl(pdfFile);

        MainActivity mainActivity = MainActivity.builder()
                .imagePath(imageUrl)
                .title(dto.getTitle())
                .pdfPath(pdfUrl)
                .explain(dto.getExplain())
                .build();

        MainActivity savedMainActivity = mainActivityRepository.save(mainActivity);
        return convertToDto(savedMainActivity);
    }

    public List<MainActivityDTO> getAllMainActivities() {
        List<MainActivity> mainActivityList = mainActivityRepository.findAll();
        return mainActivityList.stream().map(this::convertToDto).toList();
    }

    public MainActivityDTO getMainActivityById(Long id) {
        return mainActivityRepository.findById(id).map(this::convertToDto)
                .orElseThrow(() -> new FileNotFoundException("MainActivity id not found: " + id));
    }

    public MainActivityDTO updateMainActivity(Long id, MainActivityDTO dto, MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        MainActivity existingMainActivity = mainActivityRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("MainActivity id not found: " + id));

        String imageUrl = saveFileAndGetUrl(imageFile);
        String pdfUrl = saveFileAndGetUrl(pdfFile);

        MainActivity updatedMainActivity = MainActivity.builder()
                .id(existingMainActivity.getId())
                .imagePath(imageUrl)
                .title(dto.getTitle())
                .pdfPath(pdfUrl)
                .explain(dto.getExplain())
                .build();

        MainActivity savedMainActivity = mainActivityRepository.save(updatedMainActivity);
        return convertToDto(savedMainActivity);
    }

    public void deleteMainActivity(Long id) {
        if (mainActivityRepository.existsById(id)) {
            mainActivityRepository.deleteById(id);
        } else {
            throw new FileNotFoundException("MainActivity id not found: " + id);
        }
    }

    private String saveFileAndGetUrl(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        byte[] content = file.getBytes();
        objectStorageService.uploadObject(objectName, content);
        return objectStorageService.getFileUrl(objectName);
    }

    private MainActivityDTO convertToDto(MainActivity mainActivity) {
        return MainActivityDTO.builder()
                .id(mainActivity.getId())
                .imagePath(mainActivity.getImagePath())
                .title(mainActivity.getTitle())
                .pdfPath(mainActivity.getPdfPath())
                .build();
    }
}