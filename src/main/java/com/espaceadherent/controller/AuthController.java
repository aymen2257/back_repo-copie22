package com.espaceadherent.controller;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.espaceadherent.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.espaceadherent.config.AppConstants;
import com.espaceadherent.config.CurrentUser;
import com.espaceadherent.exception.UserAlreadyExistAuthenticationException;
import com.espaceadherent.model.User;
import com.espaceadherent.security.jwt.TokenProvider;
import com.espaceadherent.service.MailService;
import com.espaceadherent.service.UserService;
import com.espaceadherent.util.GeneralUtils;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")

public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	@Autowired
	TokenProvider tokenProvider;

	@Autowired
	private QrDataFactory qrDataFactory;

	@Autowired
	private QrGenerator qrGenerator;

	@Autowired
	private CodeVerifier verifier;

	@Autowired
	MailService mailService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getNum(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		LocalUser localUser = (LocalUser) authentication.getPrincipal();
		boolean authenticated = !localUser.getUser().isUsing2FA();

		// Ensure the user is enabled and has verified their email
		if (!localUser.isEnabled() || localUser.getUser().getVerified() != 2) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Account not enabled or email not verified."));
		}

		String jwt = tokenProvider.createToken(localUser, authenticated);
/*
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, true, GeneralUtils.buildUserInfo(localUser)));

*/


		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, authenticated, GeneralUtils.buildUserInfo(localUser)));


	}


	@PostMapping("/forgetPassword")
	public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) {
		try {
			User user = userService.findByEmailAndCin(forgetPasswordRequest.getEmail(),forgetPasswordRequest.getCin());
			final String token = UUID.randomUUID().toString();
			userService.createVerificationTokenForUser(user, token);
			mailService.sendVerificationToken2(token, user);

		} catch (UserAlreadyExistAuthenticationException e) {
			log.error("Exception Ocurred", e);
			return new ResponseEntity<>(new ApiResponse(false, "User Not found "), HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok().body(new ApiResponse(true, "User found successfully"));
	}


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		//User user1 =userService.findUserByNum(signUpRequest.getNum());
		if (!userService.existbynum(signUpRequest.getNum()) ) {
			return ResponseEntity
					.badRequest()
					.body(new ApiResponse(false, " num adherent non existant"));
		}
		if ( userService.findUserByNum(signUpRequest.getNum()).getVerified()!=0) {
			return ResponseEntity
					.badRequest()
					.body(new ApiResponse(false, "utilisateur deja verifie"));
		}
		try {
			User user = userService.registerNewUser(signUpRequest);
			final String token = UUID.randomUUID().toString();
			userService.createVerificationTokenForUser(user, token);
			mailService.sendVerificationToken(token, user);
			if (signUpRequest.isUsing2FA()) {
				QrData data = qrDataFactory.newBuilder().label(user.getEmail()).secret(user.getSecret()).issuer("MAE Assurances").build();
				// Generate the QR code image data as a base64 string which can
				// be used in an <img> tag:
				String qrCodeImage = getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
				return ResponseEntity.ok().body(new SignUpResponse(true, qrCodeImage));
			}
		} /*catch (UserAlreadyExistAuthenticationException e) {
			log.error("Exception Ocurred", e);
			return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
		} */
		catch (QrGenerationException e) {
			log.error("QR Generation Exception Ocurred", e);
			return new ResponseEntity<>(new ApiResponse(false, "Unable to generate QR code!"), HttpStatus.BAD_REQUEST);
		}
		/*catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Failed to verify user , account already verified: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
		}*/

		return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
	}

    @PostMapping("/changePassword")
    public ResponseEntity<?> ChangePassword(@Valid @RequestBody NewPasswordRequest newPasswordRequest) {

        User user = userService.ChangePassword(newPasswordRequest);
        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }

	@PostMapping("/verify")
	@PreAuthorize("hasRole('PRE_VERIFICATION_USER')")
	public ResponseEntity<?> verifyCode(@NotEmpty @RequestBody String code, @CurrentUser LocalUser user) {
		if (!verifier.isValidCode(user.getUser().getSecret(), code)) {
			return new ResponseEntity<>(new ApiResponse(false, "Invalid Code there is an error !"), HttpStatus.BAD_REQUEST);
		}
		String jwt = tokenProvider.createToken(user, true);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, true, GeneralUtils.buildUserInfo(user)));
	}

	@PostMapping("/verify2")
	@PreAuthorize("hasRole('PRE_VERIFICATION_USER')")
	public ResponseEntity<?> verifyCode2(@NotEmpty @RequestBody String code, @CurrentUser LocalUser user) {
		if (!verifier.isValidCode(user.getUser().getSecret(), code)) {
			return new ResponseEntity<>(new ApiResponse(false, "Invalid Code there is an error !"), HttpStatus.BAD_REQUEST);
		}
		String jwt = tokenProvider.createToken(user, true);

		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, true, GeneralUtils.buildUserInfo(user)));
	}

	@PostMapping("/token/verify")
	public ResponseEntity<?> confirmRegistration(@NotEmpty @RequestBody String token) {
		final String result = userService.validateVerificationToken(token);
		return ResponseEntity.ok().body(new ApiResponse(true, result));
	}

	// user activation - verification
	@PostMapping("/token/resend")
	@ResponseBody
	public ResponseEntity<?> resendRegistrationToken(@NotEmpty @RequestBody String expiredToken) {
		if (!userService.resendVerificationToken(expiredToken)) {
			return new ResponseEntity<>(new ApiResponse(false, "Token not found!"), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, AppConstants.SUCCESS));
	}
}