package com.espaceadherent.service;

import com.espaceadherent.model.Branche;
import com.espaceadherent.repo.BrancheRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrancheServiceImpl implements BrancheService{


    @Autowired
    private BrancheRepo brancheRepo;

    @Override
    public List<Branche> getAllBranches() {
        return brancheRepo.findAll() ;
    }

    @Override
    public Branche getBrancheById(Long id) {
        Optional<Branche> o= brancheRepo.findById(id);
        return o.isPresent() ? o.get() :null;
    }

    @Override
    public void deleteBranche(Long id) {
        brancheRepo.deleteById(id);
    }

    @Override
    public Branche updateBranche(Branche b) {
        return brancheRepo.save(b);
    }

    @Override
    public Branche findBrancheByCodeBranche(String n) {
        return brancheRepo.findBrancheByCodeBranche(n);
    }

    @Override
    public List<Branche> findBrancheByLibelleBranche(String n) {
        return brancheRepo.findBrancheByLibelleBranche(n);
    }

    @Override
    public Branche addBranche(Branche b) {
        return brancheRepo.save(b);
    }
}
