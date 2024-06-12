package com.espaceadherent.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentStatusUpdateRequest {
    // Getters and setters
    private String sessionId;
    private boolean success;

}
