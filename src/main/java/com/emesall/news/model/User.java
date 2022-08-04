package com.emesall.news.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = false)
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = -4750867559738347947L;
	
	@NotBlank
	@Include
	private String email;
	
	@NotBlank
	private String password;

	private String firstName;

	private String lastName;
	
	private boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private final Set<UserList> user_lists=new HashSet<>();

	public User() {
        super();
        this.enabled=false;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public void setPassword(PasswordEncoder passwordEncoder,String password) {
		this.setPassword(passwordEncoder.encode(password));
	}

	

}
