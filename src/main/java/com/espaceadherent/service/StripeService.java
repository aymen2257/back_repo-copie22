package com.espaceadherent.service;

import com.espaceadherent.model.Contrat;
import com.espaceadherent.model.Payment;
import com.espaceadherent.repo.PaymentRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Autowired
    private PaymentRepo paymentRepo;

    public StripeService(@Value("${stripe.api.key}") String apiKey) {
        this.apiKey = apiKey;
        Stripe.apiKey = apiKey;
    }

    private static final double TND_TO_USD_CONVERSION_RATE = 0.35; // Example conversion rate

    public Session createSession(Long amountInTND, String contractName, Long userId, Contrat contrat) throws StripeException {
        // Convert TND to USD
        Long amountInUSD = Math.round(amountInTND * TND_TO_USD_CONVERSION_RATE * 100); // Convert to cents

        String description = String.format("Montant en: %d TND", amountInTND);

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amountInUSD)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(contractName)
                                        .setDescription(description)
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        // Calculate expiration time (10 minutes from now)
        Date expirationTime = new Date(System.currentTimeMillis() + (5 * 60 * 1000));

        // Store session details in the database
        Payment payment = new Payment();
        payment.setSessionId(session.getId());
        payment.setAmount(amountInTND);
        payment.setUserId(userId);
        payment.setSignature(session.getId()); // Assuming session ID as a signature for simplicity
        payment.setDatePaiement(new Date());
        payment.setStatus("CRÉE");
        payment.setContrat(contrat);
        payment.setExpirationTime(expirationTime); // Set expiration time
        paymentRepo.save(payment);

        return session;
    }

}

