package com.espaceadherent.controller;

import com.espaceadherent.model.Agence;
import com.espaceadherent.service.AgenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/agencies")
public class AgenceController {
    @Autowired
    private AgenceService agenceService;
    @GetMapping("/governorates")
    public List<String> getGovernorates() {
        return agenceService.findDistinctGovernorates();
    }

    @GetMapping("/byGovernorate")
    public List<Agence> getAgenciesByGovernorate(@RequestParam String governorate) {
        return agenceService.findgov(governorate);
    }
    @GetMapping("/byCite")
    public List<Agence> getAgenciesByCite(@RequestParam String cite) {
        return agenceService.findByCite(cite);
    }
    @GetMapping("/nearest")
    public ResponseEntity<Agence> getNearestAgence(@RequestParam double lat, @RequestParam double lon) {
        return agenceService.findNearest(lat, lon)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/deleteAgence/{x}")

    public void deleteAgenceById(@PathVariable (name = "x") Long x) {
        agenceService.deleteAgence(x);
    }


    @PutMapping("/editagence")

    public ResponseEntity<?> EditAgence(@RequestBody Agence a)
    {
        return  ResponseEntity.ok(agenceService.editAgence(a));
    }

    @PostMapping("/addagence")
    public ResponseEntity<?> addAgence(@RequestBody Agence a)
    {
        return  ResponseEntity.ok(agenceService.addAgence(a));
    }
    @GetMapping("/findall")
    public List<Agence> findall (){
        return agenceService.findallAgence();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agence> getAgenceById(@PathVariable Long id) {
        Optional<Agence> agence = agenceService.findAgenceById(id);
        return agence.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
