package com.espaceadherent.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.espaceadherent.model.User;
import com.espaceadherent.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);

	VerificationToken findByUser(User user);
}
