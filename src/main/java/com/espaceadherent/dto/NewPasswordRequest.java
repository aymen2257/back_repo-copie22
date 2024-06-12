package com.espaceadherent.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class NewPasswordRequest {

    private String num;

    @Size(min = 6, message = "{Size.userDto.password}")
    private String password;


    public NewPasswordRequest(String num, String password) {
        this.num = num;
        this.password = password;
    }

    public static SignUpRequest.Builder getBuilder() {
        return new SignUpRequest.Builder();
    }

    public static class Builder {


        private String num;
        private String password;



        public Builder addNum(final String num) {
            this.num= num;
            return this;
        }

        public Builder addPassword(final String password) {
            this.password = password;
            return this;
        }



        public NewPasswordRequest build() {
            return new NewPasswordRequest(num,password);
        }


    }

}
