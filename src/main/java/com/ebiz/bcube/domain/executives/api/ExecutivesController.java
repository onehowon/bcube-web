package com.ebiz.bcube.domain.executives.api;

import com.ebiz.bcube.domain.executives.dto.ExecutivesDTO;
import com.ebiz.bcube.domain.executives.exception.FileNotFoundException;
import com.ebiz.bcube.domain.executives.service.ExecutiveService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/executives")
public class ExecutivesController {

    private final ExecutiveService executiveService;
    private final String bucketUrl;

    public ExecutivesController(ExecutiveService executiveService, @Value("${oracle.cloud.bucket-url}") String bucketUrl){
        this.executiveService = executiveService;
        this.bucketUrl = bucketUrl;
    }

    @PostMapping
    public ResponseEntity<ExecutivesDTO> createExecutive(
            @RequestBody ExecutivesDTO executivesDTO,
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
        try {
            ExecutivesDTO executive = executiveService.getExecutiveById(id);
            return ResponseEntity.ok(executive);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExecutivesDTO> updateExecutive(
            @PathVariable Long id,
            @RequestBody ExecutivesDTO executivesDTO,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        try {
            ExecutivesDTO updatedExecutive = executiveService.updateExecutive(id, executivesDTO, multipartFile);
            return ResponseEntity.ok(updatedExecutive);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExecutive(@PathVariable Long id) {
        try {
            executiveService.deleteExecutive(id);
            return ResponseEntity.noContent().build();
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
