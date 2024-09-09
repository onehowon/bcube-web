package com.ebiz.bcube.domain.main_activity.api;

import com.ebiz.bcube.domain.main_activity.dto.MainActivityDTO;
import com.ebiz.bcube.domain.main_activity.service.MainActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/main-activity")
public class MainActivityController {

    private final MainActivityService mainActivityService;

    @Autowired
    public MainActivityController(MainActivityService mainActivityService) {
        this.mainActivityService = mainActivityService;
    }

    @PostMapping
    public ResponseEntity<MainActivityDTO> createMainActivity(
            @ModelAttribute MainActivityDTO mainActivityDTO,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("pdf") MultipartFile pdfFile) throws IOException {
        MainActivityDTO createdMainActivity = mainActivityService.createMainActivity(mainActivityDTO, imageFile, pdfFile);
        return new ResponseEntity<>(createdMainActivity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MainActivityDTO>> getAllMainActivities() {
        List<MainActivityDTO> mainActivityList = mainActivityService.getAllMainActivities();
        return new ResponseEntity<>(mainActivityList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainActivityDTO> getMainActivityById(@PathVariable Long id) {
        MainActivityDTO mainActivity = mainActivityService.getMainActivityById(id);
        return ResponseEntity.ok(mainActivity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MainActivityDTO> updateMainActivity(
            @PathVariable Long id,
            @ModelAttribute MainActivityDTO mainActivityDTO,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("pdf") MultipartFile pdfFile) throws IOException {
        MainActivityDTO updatedMainActivity = mainActivityService.updateMainActivity(id, mainActivityDTO, imageFile, pdfFile);
        return ResponseEntity.ok(updatedMainActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMainActivity(@PathVariable Long id) {
        mainActivityService.deleteMainActivity(id);
        return ResponseEntity.noContent().build();
    }
}