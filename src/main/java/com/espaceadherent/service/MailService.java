package com.espaceadherent.service;

import com.espaceadherent.model.User;

public interface MailService {

	void sendVerificationToken(String token, User user);
	void sendVerificationToken2(String token, User user);

}
