package com.espaceadherent.repo;

import com.espaceadherent.model.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Reclamationrepo extends JpaRepository<Reclamation, Long> {
List<Reclamation> findByUserId(Long id);
}
