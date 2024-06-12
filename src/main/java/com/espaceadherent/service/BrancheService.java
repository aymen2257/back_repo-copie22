package com.espaceadherent.service;

import com.espaceadherent.model.Branche;

import java.util.List;

public interface BrancheService {
    public List<Branche> getAllBranches();

    public Branche getBrancheById(Long id);

    public void deleteBranche(Long id);

    public Branche updateBranche(Branche b);

    public Branche findBrancheByCodeBranche(String n);

    public List<Branche> findBrancheByLibelleBranche(String n);

    public Branche addBranche(Branche b);
}
