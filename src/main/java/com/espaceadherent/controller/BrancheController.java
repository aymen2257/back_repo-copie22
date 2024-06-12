package com.espaceadherent.controller;

import com.espaceadherent.model.Branche;
import com.espaceadherent.service.BrancheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BrancheController {

    @Autowired
    private BrancheService brancheService;
    
    @GetMapping("branches")
    public ResponseEntity<?> getAllBranches()
    {
        return  ResponseEntity.ok(brancheService.getAllBranches());
    }

    @GetMapping("/branche/{id}")
    public ResponseEntity<?> getBrancheById(@PathVariable(name = "id") Long id)
    {
        return  ResponseEntity.ok(brancheService.getBrancheById(id));
    }

    @GetMapping("/branches/{libelle}")
    public List<Branche> getBrancheByLibelle(@PathVariable (name = "libelle") String libelle) {
        return brancheService.findBrancheByLibelleBranche(libelle);
    }

    @GetMapping("/branche/code/{code}")
    public Branche getBrancheByCode(@PathVariable (name = "code") String code) {
        return brancheService.findBrancheByCodeBranche(code);
    }


    @DeleteMapping("/deleteBranche/{x}")
    public void deleteBrancheById(@PathVariable (name = "x") Long x) {
        brancheService.deleteBranche(x);
    }


    @PutMapping("editbranche")
    public ResponseEntity<?> EditBranche(@RequestBody Branche b)
    {
        return  ResponseEntity.ok(brancheService.updateBranche(b));
    }

    @PostMapping("addbranche")
    public ResponseEntity<?> addBranche(@RequestBody Branche b)
    {
        return  ResponseEntity.ok(brancheService.addBranche(b));
    }
}
