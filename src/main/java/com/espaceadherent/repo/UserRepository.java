package com.espaceadherent.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espaceadherent.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByEmailAndCin (String email, Long cin);


	User findByNum(String num);



	boolean existsByNum(String num);
	boolean existsByEmail(String email);


}
