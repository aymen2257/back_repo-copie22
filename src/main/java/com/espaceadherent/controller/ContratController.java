package com.espaceadherent.controller;


import com.espaceadherent.dto.PaymentStatusUpdateRequest;
import com.espaceadherent.model.Contrat;
import com.espaceadherent.model.CountType;
import com.espaceadherent.model.Payment;
import com.espaceadherent.repo.PaymentRepo;
import com.espaceadherent.service.ContratService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/contrat")
@CrossOrigin("*")
public class ContratController {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private ContratService contratService;

    @GetMapping("listeContrats")
    public ResponseEntity<?> getAllContrats()
    {
        return  ResponseEntity.ok(contratService.getAllContarts());
    }

    @GetMapping("PercentageByBranche")
    public ResponseEntity<?> getPercentageByBranche()
    {
        return  ResponseEntity.ok(contratService.getPercentageGroupByBranche());
    }

    @GetMapping("Count")
    public Map<String,Long > countByVisitType() {
        Map<String, Long> result = new HashMap<>();
        List<CountType> counts = contratService.getPercentageGroupByBranche();
        for (CountType row : counts) {
            result.put( row.getBranche().getLibelleBranche(),row.getCount());
        }
        return result;
    }


    @GetMapping("/{id}")
    public Contrat getContratById(@PathVariable(name = "id") Long id) {
        return contratService.getContartById(id);
    }


    @GetMapping("/Mycontrat/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getContratByUserId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(contratService.findContratByUserId(id));
    }


    @DeleteMapping("/{x}")
    public void deleteoffreReservationById(@PathVariable (name = "x") Long x) {
        contratService.deleteContart(x);}


    @GetMapping("/countByProduit")
    public ResponseEntity<Map<String, Long>> getCountByProduit() {
        return ResponseEntity.ok(contratService.getContratCountByProduit());
    }
    @PostMapping("/pay/{contratId}")
    public ResponseEntity<Map<String, String>> payForContrat(@PathVariable Long contratId) {
        try {
            String sessionId = contratService.createPaymentSession(contratId);
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Payment processing error: " + e.getMessage()));
        }
    }


    @PostMapping("/update-payment-status")
    public ResponseEntity<Map<String, String>> updatePaymentStatus(@RequestBody PaymentStatusUpdateRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            contratService.updatePaymentStatus(request.getSessionId(), request.isSuccess());
            response.put("message", "Payment status updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error updating payment status: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/expired-contracts/{userId}")
    public ResponseEntity<?> getExpiredContracts(@PathVariable Long userId) {
        try {
            List<Contrat> expiredContracts = contratService.findExpiredContracts(userId);
            return ResponseEntity.ok(expiredContracts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching expired contracts: " + e.getMessage());
        }
    }

    @PostMapping("/renew-contract/{contratId}")
    public ResponseEntity<?> renewContract(@PathVariable Long contratId) {
        try {
            String sessionId = contratService.renewContract(contratId);
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error renewing contract: " + e.getMessage());
        }
    }
    @GetMapping("/payments/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPaymentsByUserId(@PathVariable Long userId) {
        try {
            List<Payment> payments = paymentRepo.findByUserId(userId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching payments: " + e.getMessage());
        }
    }
}
