package com.espaceadherent.controller;

import com.espaceadherent.model.Reclamation;
import com.espaceadherent.model.ReclamationStatus;
import com.espaceadherent.service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file, @Valid Reclamation reclamation) {
        try {
            reclamationService.saveReclamation(file, reclamation);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "File uploaded and form data saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/getall")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        List<Reclamation> reclamations = reclamationService.getAllReclamations();
        if (reclamations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reclamations);
    }
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateReclamationStatus(@PathVariable Long id, @PathVariable ReclamationStatus status) {
        try {
            Reclamation updatedReclamation = reclamationService.updateReclamationStatus(id, status);
            return ResponseEntity.ok(updatedReclamation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Reclamation reclamation = reclamationService.getFileData(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(reclamation.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file_" + id + "\"")
                .body(reclamation.getFileData());
    }
    @GetMapping("/my-reclamations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Reclamation>> getUserReclamations() {
        List<Reclamation> reclamations = reclamationService.getReclamationsByUser();
        if (reclamations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reclamations);
    }
}
