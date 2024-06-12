package com.espaceadherent.controller;


import com.espaceadherent.model.User;
import com.espaceadherent.repo.UserRepository;
import com.espaceadherent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.espaceadherent.config.CurrentUser;
import com.espaceadherent.dto.LocalUser;
import com.espaceadherent.util.GeneralUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
		return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
	}

	@GetMapping("/getUserById/{id}")
	public ResponseEntity<?> getUserById(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}



	@PutMapping("/changeImage")
	public ResponseEntity<?> changeImage(@RequestParam("fileImage") MultipartFile fileImage,@CurrentUser LocalUser user) throws IOException
	{
		try {
			User user1= userService.getUserById(user.getUser().getId());
			if (fileImage.isEmpty()) {
				throw new IllegalArgumentException("image cannot be empty");
			}
			byte[] imageBytes = fileImage.getBytes();
			user1.setImage(imageBytes);
			Date now = Calendar.getInstance().getTime();
			user1.setModifiedDate(now);
			userRepository.save(user1);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "image changed successfully");
			return ResponseEntity.ok(response);  // Send JSON response
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Failed to upload file: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
		}
	}




	@PutMapping("/changePwd")
	public ResponseEntity<?> changePassword(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd, @CurrentUser LocalUser user) {
		try {
			User user1 = userService.getUserById(user.getUser().getId());
			if (!passwordEncoder.matches(oldPwd, user1.getPassword())) {
				Map<String, Object> response = new HashMap<>();
				response.put("error", "Current password is incorrect");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
			}
			user1.setPassword(passwordEncoder.encode(newPwd));
			Date now = Calendar.getInstance().getTime();
			user1.setModifiedDate(now);
			userRepository.save(user1);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Password changed successfully");
			return ResponseEntity.ok(response);  // Send JSON response
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Failed to change password: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
		}
	}


	@PutMapping("/changeData")
	public ResponseEntity<?> changeData(@RequestParam("nom") String nom,@RequestParam("prenom") String prenom, @RequestParam("email") String email, @RequestParam("address") String address, @RequestParam("sex") String sex, @RequestParam("date") @DateTimeFormat (pattern = "yyyy-MM-dd") Date date, @CurrentUser LocalUser user)
	{
		try {
			User user1= userService.getUserById(user.getUser().getId());
			user1.setNom(nom);
			user1.setPrenom(prenom);
			user1.setEmail(email);
			user1.setAddress(address);
			user1.setSex(sex);
			user1.setBirth_date(date);
			Date now = Calendar.getInstance().getTime();
			user1.setModifiedDate(now);
			userRepository.save(user1);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "user data changed successfully");
			return ResponseEntity.ok(response);  // Send JSON response
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Failed to change user data: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
		}
	}

	@PutMapping("/desactiverUser")
	public ResponseEntity<?> DesactiverUser( @CurrentUser LocalUser user){
		try {
			userService.desactiverUser(user.getUser());
			Map<String, Object> response = new HashMap<>();
			response.put("message", "user account desactivated successfully");
			return ResponseEntity.ok(response);  // Send JSON response
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Failed to desactivate user account: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
		}
	}


	@PutMapping("/reactiverUser")
	public ResponseEntity<?> ReactiverUser(@RequestBody User user){
		try {
			userService.reactiverUser(user);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "user account reactivated successfully");
			return ResponseEntity.ok(response);  // Send JSON response
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Failed to reactivate user account: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Send JSON response
		}
	}








	@GetMapping("listeUsers")
	public ResponseEntity<?> getAllUserss()
	{
		return  ResponseEntity.ok(userService.findAllUsers());
	}

	@GetMapping("/all")
	public ResponseEntity<?> getContent() {
		return ResponseEntity.ok("Public content goes here");
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserContent() {
		return ResponseEntity.ok("User content goes here");
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAdminContent() {
		return ResponseEntity.ok("Admin content goes here");
	}


}