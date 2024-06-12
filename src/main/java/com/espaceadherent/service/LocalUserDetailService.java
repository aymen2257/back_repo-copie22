package com.espaceadherent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espaceadherent.dto.LocalUser;
import com.espaceadherent.exception.ResourceNotFoundException;
import com.espaceadherent.model.User;
import com.espaceadherent.util.GeneralUtils;

/**

 */
@Service("localUserDetailService")
public class LocalUserDetailService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public LocalUser loadUserByUsername(final String num) throws UsernameNotFoundException {
		User user = userService.findUserByNum(num);
		if (user == null) {
			throw new UsernameNotFoundException("User " + num + " was not found in the database");
		}

		return createLocalUser(user);
	}

	@Transactional
	public LocalUser loadUserById(Long id) {
		User user = userService.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		return createLocalUser(user);
	}


	private LocalUser createLocalUser(User user) {
		return new LocalUser(user.getEmail(), user.getPassword(), user.isEnabled(), user.getVerified(), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities(user.getRoles()), user);
	}
}
