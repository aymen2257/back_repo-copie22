package com.espaceadherent.repo;

import com.espaceadherent.model.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Agencerepo extends JpaRepository<Agence,Long> {
    List<Agence> findByGovernorate(String governate);
    List<Agence> findByCite(String cite);
    @Query("SELECT DISTINCT a.governorate FROM Agence a")
    List<String> findDistinctGovernorates();
    @Query(value = "SELECT * FROM Agence ORDER BY ST_Distance_Sphere(POINT(longitude, latitude), POINT(:lon, :lat)) LIMIT 1", nativeQuery = true)
    Agence findNearest(double lat, double lon);

}
