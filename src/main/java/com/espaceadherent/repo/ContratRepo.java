package com.espaceadherent.repo;

import com.espaceadherent.model.Contrat;
import com.espaceadherent.model.CountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContratRepo extends JpaRepository<Contrat,Long> {

    List<Contrat> findContratByUserId(Long n);

    @Query(value = "SELECT new com.espaceadherent.model.CountType(count(*)/(SELECT COUNT(*) FROM Contrat)*100,c.branche)FROM Contrat c GROUP BY c.branche")
    public List<CountType> getPercentageGroupByBranche();

    /*@Query("SELECT p.id, COUNT(c) FROM Contrat c JOIN c.produit p GROUP BY p.id")
    List<Object[]> countContratByProduit();*/

    @Query("SELECT p.Nom, COUNT(c) FROM Contrat c JOIN c.produit p GROUP BY p.Nom")
    List<Object[]> countContratByProduit();
    @Query("SELECT c FROM Contrat c WHERE c.user.id = :userId AND c.date_fin_effet < :currentDate")
    List<Contrat> findExpiredContracts(@Param("userId") Long userId, @Param("currentDate") Date currentDate);

}
