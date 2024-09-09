package com.ebiz.bcube.domain.activities.api;

import com.ebiz.bcube.domain.activities.dto.DesigntonDTO;
import com.ebiz.bcube.domain.activities.dto.EtcDTO;
import com.ebiz.bcube.domain.activities.dto.SexyItDTO;
import com.ebiz.bcube.domain.activities.dto.StudyDTO;
import com.ebiz.bcube.domain.activities.service.DesigntonService;
import com.ebiz.bcube.domain.activities.service.EtcService;
import com.ebiz.bcube.domain.activities.service.SexyItService;
import com.ebiz.bcube.domain.activities.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivitiesController {

    private final DesigntonService designtonService;
    private final EtcService etcService;
    private final SexyItService sexyItService;
    private final StudyService studyService;

    @Autowired
    public ActivitiesController(
            DesigntonService designtonService,
            EtcService etcService,
            SexyItService sexyItService,
            StudyService studyService) {
        this.designtonService = designtonService;
        this.etcService = etcService;
        this.sexyItService = sexyItService;
        this.studyService = studyService;
    }

    // 디자인톤 엔드포인트
    @PostMapping("/designton")
    public ResponseEntity<DesigntonDTO> createDesignton(
            @ModelAttribute DesigntonDTO designtonDTO,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("pdf") MultipartFile pdfFile) throws IOException{
        DesigntonDTO createdDesignton = designtonService.createDesignton(designtonDTO, imageFile, pdfFile);
        return new ResponseEntity<>(createdDesignton, HttpStatus.CREATED);
    }

    @GetMapping("/designton")
    public ResponseEntity<List<DesigntonDTO>> getAllDesignton(){
        List<DesigntonDTO> designtonList = designtonService.getAllDesignton();
        return new ResponseEntity<>(designtonList, HttpStatus.OK);
    }

    @GetMapping("/designton/{id}")
    public ResponseEntity<DesigntonDTO> getDesigntonById(@PathVariable Long id){
        DesigntonDTO designton = designtonService.getDesigntonById(id);
        return ResponseEntity.ok(designton);
    }

    @PutMapping("/designton/{id}")
    public ResponseEntity<DesigntonDTO> updateDesignton(
            @PathVariable Long id,
            @ModelAttribute DesigntonDTO designtonDTO,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("pdf") MultipartFile pdfFile) throws IOException{
        DesigntonDTO updatedDesignton = designtonService.updateDesignton(id, designtonDTO, imageFile, pdfFile);
        return ResponseEntity.ok(updatedDesignton);
    }

    @DeleteMapping("/designton/{id}")
    public ResponseEntity<Void> deleteDesignton(@PathVariable Long id) {
        designtonService.deleteDesignton(id);
        return ResponseEntity.noContent().build();
    }

    // 기타활동 엔드포인트
    @PostMapping("/etc")
    public ResponseEntity<EtcDTO> createEtc(
            @ModelAttribute EtcDTO etcDTO,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        EtcDTO createdEtc = etcService.createEtc(etcDTO, imageFile);
        return new ResponseEntity<>(createdEtc, HttpStatus.CREATED);
    }

    @GetMapping("/etc")
    public ResponseEntity<List<EtcDTO>> getAllEtc() {
        List<EtcDTO> etcList = etcService.getAllEtc();
        return new ResponseEntity<>(etcList, HttpStatus.OK);
    }

    @GetMapping("/etc/{id}")
    public ResponseEntity<EtcDTO> getEtcById(@PathVariable Long id) {
        EtcDTO etc = etcService.getEtcById(id);
        return ResponseEntity.ok(etc);
    }

    @PutMapping("/etc/{id}")
    public ResponseEntity<EtcDTO> updateEtc(
            @PathVariable Long id,
            @ModelAttribute EtcDTO etcDTO,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        EtcDTO updatedEtc = etcService.updateEtc(id, etcDTO, imageFile);
        return ResponseEntity.ok(updatedEtc);
    }

    @DeleteMapping("/etc/{id}")
    public ResponseEntity<Void> deleteEtc(@PathVariable Long id) {
        etcService.deleteEtc(id);
        return ResponseEntity.noContent().build();
    }

    // 섹시한IT 엔드포인트
    @PostMapping("/sexyit")
    public ResponseEntity<SexyItDTO> createSexyIt(
            @ModelAttribute SexyItDTO sexyItDTO,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        SexyItDTO createdSexyIt = sexyItService.createSexyIt(sexyItDTO, imageFile);
        return new ResponseEntity<>(createdSexyIt, HttpStatus.CREATED);
    }

    @GetMapping("/sexyit")
    public ResponseEntity<List<SexyItDTO>> getAllSexyIt() {
        List<SexyItDTO> sexyItList = sexyItService.getAllSexyIt();
        return new ResponseEntity<>(sexyItList, HttpStatus.OK);
    }

    @GetMapping("/sexyit/{id}")
    public ResponseEntity<SexyItDTO> getSexyItById(@PathVariable Long id) {
        SexyItDTO sexyIt = sexyItService.getSexyItById(id);
        return ResponseEntity.ok(sexyIt);
    }

    @PutMapping("/sexyit/{id}")
    public ResponseEntity<SexyItDTO> updateSexyIt(
            @PathVariable Long id,
            @ModelAttribute SexyItDTO sexyItDTO,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        SexyItDTO updatedSexyIt = sexyItService.updateSexyIt(id, sexyItDTO, imageFile);
        return ResponseEntity.ok(updatedSexyIt);
    }

    @DeleteMapping("/sexyit/{id}")
    public ResponseEntity<Void> deleteSexyIt(@PathVariable Long id) {
        sexyItService.deleteSexyIt(id);
        return ResponseEntity.noContent().build();
    }

    // 스터디 엔드포인트
    @PostMapping("/study")
    public ResponseEntity<StudyDTO> createStudy(
            @ModelAttribute StudyDTO studyDTO,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        StudyDTO createdStudy = studyService.createStudy(studyDTO, imageFile);
        return new ResponseEntity<>(createdStudy, HttpStatus.CREATED);
    }

    @GetMapping("/study")
    public ResponseEntity<List<StudyDTO>> getAllStudy() {
        List<StudyDTO> studyList = studyService.getAllStudy();
        return new ResponseEntity<>(studyList, HttpStatus.OK);
    }

    @GetMapping("/study/{id}")
    public ResponseEntity<StudyDTO> getStudyById(@PathVariable Long id) {
        StudyDTO study = studyService.getStudyById(id);
        return ResponseEntity.ok(study);
    }

    @PutMapping("/study/{id}")
    public ResponseEntity<StudyDTO> updateStudy(
            @PathVariable Long id,
            @ModelAttribute StudyDTO studyDTO,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        StudyDTO updatedStudy = studyService.updateStudy(id, studyDTO, imageFile);
        return ResponseEntity.ok(updatedStudy);
    }

    @DeleteMapping("/study/{id}")
    public ResponseEntity<Void> deleteStudy(@PathVariable Long id) {
        studyService.deleteStudy(id);
        return ResponseEntity.noContent().build();
    }
}
