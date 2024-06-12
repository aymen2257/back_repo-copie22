package com.espaceadherent.model;

import java.io.Serializable;
import java.util.Date;
//import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 65981149772133526L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;

	@Column(name = "num_member")
	private String num;


	private String email;

	private String address;

	private Long cin;

	private String sex;

	@Lob
	@Column(name = "image_file", columnDefinition="LONGBLOB")
	private byte[] image;

	/*@Temporal(TemporalType.TIMESTAMP)
	private Date birth_date;*/

	@Column(name = "dateNaissance")
	private Date birth_date;

	@Column(columnDefinition = "BIT", length = 1)
	private boolean enabled=false;

	@Column(name = "NOM")
	private String nom;

	@Column(name = "PRENOM")
	private String prenom;

	@Column(name = "created_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date modifiedDate;

	private String password;

	@Column(name = "USING_2FA")
	private boolean using2FA=false;

	private String secret;

	// bi-directional many-to-many association to Role
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "USER_ID")}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
	private Set<Role> roles;
	@Column(name = "verified")
	private Byte verified =0; // Default is 0, indicating not verified. Use other values to indicate different states.
}