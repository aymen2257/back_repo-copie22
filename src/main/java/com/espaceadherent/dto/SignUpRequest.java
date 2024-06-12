package com.espaceadherent.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.espaceadherent.validator.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches
public class SignUpRequest {

	private Long userID;


	private String displayName;

	private String num;

	private Long cin ;

	@NotEmpty
	private String email;

	@Size(min = 6, message = "{Size.userDto.password}")
	private String password;

	@NotEmpty
	private String matchingPassword;

	private boolean using2FA;


	public SignUpRequest(String num,Long cin, String email, String password, String matchingPassword ) {
        this.num=num;
		this.cin=cin;
		this.email = email;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}



	public static Builder getBuilder() {
		return new Builder();
	}

	public static class Builder {

		private String displayName;


		private Long cin ;
		private String email;

		private String num ;
		private String password;

		private String matchingPassword;




		public Builder addEmail(final String email) {
			this.email = email;
			return this;
		}

		public Builder addNum(final String num) {
			this.num= num;
			return this;
		}

		public Builder addPassword(final String password) {
			this.password = password;
			return this;
		}

		public Builder addMatchingPassword(final String matchingPassword) {
			this.matchingPassword = matchingPassword;
			return this;
		}



		public Builder addDisplayName(String name) {
			this.displayName=name;
			return this;
		}

		public Builder addCin(final Long cin) {
			this.cin= cin;
			return this;
		}




		public SignUpRequest build() {
			return new SignUpRequest(num, cin,email, password, matchingPassword);
		}



	}
}
