package com.espaceadherent.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ForgetPasswordRequest {

    private Long cin ;

    @NotEmpty
    private String email;


    public ForgetPasswordRequest(String email,Long cin) {
        this.email=email;
        this.cin=cin;

    }


    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Long cin ;
        private String email;





        public Builder addEmail(final String email) {
            this.email = email;
            return this;
        }


        public Builder addCin(final Long cin) {
            this.cin= cin;
            return this;
        }

        public ForgetPasswordRequest build() {
            return new ForgetPasswordRequest(email,cin);
        }



    }
}
