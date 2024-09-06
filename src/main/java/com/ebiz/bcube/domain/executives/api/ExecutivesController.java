package com.ebiz.bcube.domain.executives.api;

import com.ebiz.bcube.domain.executives.dto.ExecutivesDTO;
import com.ebiz.bcube.domain.executives.service.ExecutiveService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/executives")
public class ExecutivesController {

    private final ExecutiveService executiveService;
    private final String bucketUrl;

    public ExecutivesController(ExecutiveService executiveService, @Value("${oracle.cloud.bucket-url}") String bucketUrl){
        this.executiveService = executiveService;
        this.bucketUrl = bucketUrl;  // 필요하다면 사용
    }

    @PostMapping
    public ResponseEntity<ExecutivesDTO> createExecutive(
            @ModelAttribute ExecutivesDTO executivesDTO,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {

        ExecutivesDTO createdExecutive = executiveService.createExecutive(executivesDTO, multipartFile);
        return new ResponseEntity<>(createdExecutive, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExecutivesDTO>> getAllExecutives() {
        List<ExecutivesDTO> executives = executiveService.getAllExecutives();
        return new ResponseEntity<>(executives, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExecutivesDTO> getExecutiveById(@PathVariable Long id) {
        ExecutivesDTO executive = executiveService.getExecutiveById(id);
        return ResponseEntity.ok(executive);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExecutivesDTO> updateExecutive(
            @PathVariable Long id,
            @ModelAttribute ExecutivesDTO executivesDTO,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        ExecutivesDTO updatedExecutive = executiveService.updateExecutive(id, executivesDTO, multipartFile);
        return ResponseEntity.ok(updatedExecutive);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExecutive(@PathVariable Long id) {
        executiveService.deleteExecutive(id);
        return ResponseEntity.noContent().build();
    }
}