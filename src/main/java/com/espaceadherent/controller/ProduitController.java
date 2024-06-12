package com.espaceadherent.controller;

import com.espaceadherent.model.Branche;
import com.espaceadherent.model.Produit;
import com.espaceadherent.repo.BrancheRepo;
import com.espaceadherent.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin("*")
public class ProduitController {
    @Autowired
    private BrancheRepo brancheRepo;

    @Autowired
    private ProduitService produitService;

    @PostMapping("/addProduit")
    public ResponseEntity<?> addProduit(@RequestParam("fileIcon") MultipartFile fileIcon,@RequestParam("file") MultipartFile file,@RequestParam("brancheId") Long brancheId, @Valid Produit produit) {
        try {
            produitService.addProduct(fileIcon,file,produit, brancheId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "icon and image uploaded and form data saved successfully");
            return ResponseEntity.ok(response);  // Send JSON response
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
        }
    }


    @PutMapping("/editProduit/{brancheId}")
    public ResponseEntity<?> EditProduit(@RequestParam("id") Long id,@RequestParam("fileIcon") MultipartFile fileIcon, @RequestParam("file") MultipartFile file,@RequestParam("nom") String nom,@RequestParam("titre") String titre,@RequestParam("description") String description,@RequestParam("contenu") String contenu, @PathVariable("brancheId") Long brancheId) {
        try {
            Produit produit=produitService.getProduitById(id);
            produit.setNom(nom);
            produit.setTitre(titre);
            produit.setContenu(contenu);
            produit.setDescription(description);
            Branche branche = brancheRepo.findById(brancheId).orElseThrow(() -> new IllegalArgumentException("Invalid branche ID"));
            produit.setBranche(branche);
            byte[] iconBytes = fileIcon.getBytes();
            produit.setIcon(iconBytes);
            produit.setImage(file.getBytes());
            produitService.EditProduct(produit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "icon and image uploaded and form data saved successfully");
            return ResponseEntity.ok(response);  // Send JSON response
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
        }
    }

    @GetMapping("/getproduit/{id}")
    public ResponseEntity<?> getBrancheById(@PathVariable(name = "id") Long id)
    {
        return  ResponseEntity.ok(produitService.getProduitById(id));
    }


    @GetMapping
    @RequestMapping("/getallProduits")
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitService.getAllProduits();
        if (produits.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produits);
    }

    @DeleteMapping("/deleteProduit/{x}")
    public void deleteBrancheById(@PathVariable (name = "x") Long x) {

        produitService.deleteProduct(x);
    }


}
