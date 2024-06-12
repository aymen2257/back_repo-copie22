package com.espaceadherent.service;

import java.util.List;
import java.util.Optional;

import com.espaceadherent.dto.NewPasswordRequest;

import com.espaceadherent.dto.SignUpRequest;
import com.espaceadherent.exception.UserAlreadyExistAuthenticationException;
import com.espaceadherent.model.User;


public interface UserService {

	public List<User> findAllUsers() ;

	public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

	User findUserByEmail(String email);

	User ChangePassword (NewPasswordRequest newPasswordRequest);

	User findByEmailAndCin(String email,Long cin);

	User findUserByNum(String num);

	User getUserById (Long id);

	Optional<User> findUserById(Long id);

	/*LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);*/
	
	void createVerificationTokenForUser(User user, String token);

	boolean resendVerificationToken(String token);

	String validateVerificationToken(String token);

	boolean existbynum(String num);

	void desactiverUser(User user);

	void reactiverUser(User user);


}
