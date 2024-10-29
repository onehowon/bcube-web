package com.ebiz.bcube.domain.main_image.api;

import com.ebiz.bcube.domain.main_image.dto.MainImageDTO;
import com.ebiz.bcube.domain.main_image.service.MainImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/main-image")
@RequiredArgsConstructor
public class MainImageController {
    private final MainImageService mainImageService;
    @PostMapping
    public ResponseEntity<MainImageDTO> createMainImage(@RequestParam("file") MultipartFile multipartFile) {
        try {
            MainImageDTO mainImageDTO = mainImageService.createMainImage(multipartFile);
            return new ResponseEntity<>(mainImageDTO, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<MainImageDTO>> getAllMainImages() {
        List<MainImageDTO> mainImages = mainImageService.getAllMainImages();
        return new ResponseEntity<>(mainImages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainImageDTO> getMainImageById(@PathVariable Long id) {
        MainImageDTO mainImageDTO = mainImageService.getMainImageById(id);
        if (mainImageDTO != null) {
            return new ResponseEntity<>(mainImageDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<MainImageDTO> updateMainImage(@PathVariable Long id, @RequestParam("file") MultipartFile multipartFile) {
        try {
            MainImageDTO updatedMainImageDTO = mainImageService.updateMainImage(id, multipartFile);
            return new ResponseEntity<>(updatedMainImageDTO, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMainImage(@PathVariable Long id) {
        mainImageService.deleteMainImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
