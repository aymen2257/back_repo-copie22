package com.espaceadherent.service;

import com.espaceadherent.model.Contrat;
import com.espaceadherent.model.CountType;

import java.util.List;
import java.util.Map;

public interface ContratService {

    public List<Contrat> getAllContarts();

    public List<Contrat> findContratByUserId(Long id);


    public Contrat getContartById(Long id);

    public void deleteContart(Long id);

    public List<CountType> getPercentageGroupByBranche();
    public String createPaymentSession(Long contratId) throws Exception;

    Map<String, Long> getContratCountByProduit();
    public void updatePaymentStatus(String sessionId, boolean success) throws Exception;
    public String renewContract(Long contratId) throws Exception;
    public List<Contrat> findExpiredContracts(Long userId);
    public void cleanUpExpiredSessions();

   /*  public void handlePaymentSuccess(String sessionId);
   public String createPaymentSession(Long contratId, Long userId, String signature) throws Exception;*/
}
