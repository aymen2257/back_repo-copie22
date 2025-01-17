package com.espaceadherent.config;

import java.util.Set;

import com.espaceadherent.model.Role;
import com.espaceadherent.model.User;

public class MockUserUtils {

	private MockUserUtils() {
	}
	/**
	 * 
	 */
	public static User getMockUser(String username) {
		User user = new User();
		user.setId(1L);
		user.setEmail(username);
		user.setPassword("secret");
		Role role = new Role();
		role.setName(Role.ROLE_PRE_VERIFICATION_USER);
		user.setRoles(Set.of(role));
		user.setEnabled(true);
		user.setSecret("secret");
		return user;
	}
}
