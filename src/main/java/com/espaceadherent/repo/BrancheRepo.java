package com.espaceadherent.repo;

import com.espaceadherent.model.Branche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BrancheRepo  extends JpaRepository<Branche,Long> {

    Branche findBrancheByCodeBranche(String n);
    List<Branche> findBrancheByLibelleBranche(String n);

}
