package com.espaceadherent.service;

import java.util.*;

import java.util.Calendar;
import java.util.List;


import com.espaceadherent.dto.NewPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espaceadherent.config.AppConstants;
import com.espaceadherent.dto.SignUpRequest;
import com.espaceadherent.model.Role;
import com.espaceadherent.model.User;
import com.espaceadherent.model.VerificationToken;
import com.espaceadherent.repo.RoleRepository;
import com.espaceadherent.repo.UserRepository;
import com.espaceadherent.repo.VerificationTokenRepository;

import com.espaceadherent.util.GeneralUtils;

import dev.samstevens.totp.secret.SecretGenerator;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SecretGenerator secretGenerator;

	@Autowired
	MailService mailService;

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(value = "transactionManager")
	public User registerNewUser(final SignUpRequest signUpRequest) /*throws UserAlreadyExistAuthenticationException */{
		/*if ((signUpRequest.getUserID() == null && !userRepository.existsById(signUpRequest.getUserID()))) {
			throw new UserAlreadyExistAuthenticationException("member with User id :" + signUpRequest.getUserID() + "not exist");
		}else*/
		/* else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
		}else */
			/*if (!userRepository.existsByNum(signUpRequest.getNum()) ){
				throw new UserAlreadyExistAuthenticationException("member with User num  : "+signUpRequest.getNum() + " not exist");
			}*/


		User user = buildUser(signUpRequest);
		Date now = Calendar.getInstance().getTime();
		user.setCreatedDate(now);
		user.setModifiedDate(now);
		user = userRepository.save(user);
		userRepository.flush();
		return user;



	}
	public boolean existbynum(String num) {
		// Assuming you have a memberRepository
		return userRepository.existsByNum(num);
	}

	@Override
	public void desactiverUser(User user) {
		user.setEnabled(false);
		user.setVerified((byte)3);
		userRepository.save(user);
	}

	@Override
	public void reactiverUser(User user) {
		user.setEnabled(true);
		user.setVerified((byte)2);
		userRepository.save(user);
	}


	private User buildUser(final SignUpRequest formDTO) {
		User user = findUserByNum(formDTO.getNum());
		user.setNum(formDTO.getNum());
		user.setEmail(formDTO.getEmail());
		user.setCin(formDTO.getCin());
		user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
		final HashSet<Role> roles = new HashSet<Role>();
		roles.add(roleRepository.findByName(Role.ROLE_USER));
		user.setRoles(roles);
		user.setEnabled(user.isEnabled());
		user.setVerified((byte)1);
		if (formDTO.isUsing2FA()) {
			user.setUsing2FA(true);
			user.setSecret(secretGenerator.generate());
		}
		return user;
	}

	@Override
	@Transactional(value = "transactionManager")
	public User ChangePassword (final NewPasswordRequest newPasswordRequest){
		User user = findUserByNum(newPasswordRequest.getNum());
		user.setPassword(passwordEncoder.encode(newPasswordRequest.getPassword()));
		Date now = Calendar.getInstance().getTime();
		user.setModifiedDate(now);
		user = userRepository.save(user);
		userRepository.flush();
		return user;
	}

	@Override
	public User findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByEmailAndCin(String email, Long cin) {
		return userRepository.findByEmailAndCin(email,cin);
	}

	@Override
	public User findUserByNum(final String num) {
		return userRepository.findByNum(num);
	}

	@Override
	public User getUserById(Long id) {
		Optional<User> o= userRepository.findById(id);
		return o.isPresent() ? o.get() :null;
	}




	@Override
	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		final VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	@Override
	@Transactional
	public boolean resendVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
		if(vToken != null) {
			vToken.updateToken(UUID.randomUUID().toString());
			vToken = tokenRepository.save(vToken);
			mailService.sendVerificationToken(vToken.getToken(), vToken.getUser());
			return true;
		}
		return false;
	}

	@Override
	public String validateVerificationToken(String token) {
		final VerificationToken verificationToken = tokenRepository.findByToken(token);
		if (verificationToken == null) {
			return AppConstants.TOKEN_INVALID;
		}

		final User user = verificationToken.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return AppConstants.TOKEN_EXPIRED;
		}
		if (user.getVerified() == 1) {
			user.setEnabled(true);
			user.setVerified((byte)2);  // Email verified, update status
		}


		tokenRepository.delete(verificationToken);
		userRepository.save(user);
		return AppConstants.TOKEN_VALID;
	}


}
