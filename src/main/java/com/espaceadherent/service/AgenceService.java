package com.espaceadherent.service;

import com.espaceadherent.model.Agence;
import com.espaceadherent.repo.Agencerepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgenceService {
    @Autowired
    private Agencerepo repo;
    public List<Agence> findgov(String governorate) {
        return repo.findByGovernorate(governorate);
    }
    public List<String> findDistinctGovernorates() {
        return repo.findDistinctGovernorates();
    }

    public Optional<Agence> findNearest(double latitude, double longitude) {
        return Optional.ofNullable(repo.findNearest(latitude, longitude));
    }
    public List<Agence> findByCite(String cite) {
        return repo.findByCite(cite);
    }
    public Agence addAgence(Agence a){ return repo.save(a);}
    public Agence editAgence(Agence a){return repo.save(a);}
    public void deleteAgence(Long id){repo.deleteById(id);}
    public List<Agence> findallAgence(){return repo.findAll();}
    public Optional<Agence> findAgenceById(Long id) {
        return repo.findById(id);
    }

}
