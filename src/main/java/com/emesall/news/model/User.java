package com.emesall.news.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = -4750867559738347947L;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;

	private String firstName;

	private String lastName;
	
	private boolean enabled;
	
	@ManyToMany
	@JoinTable(name = "user_category", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns=@JoinColumn(name = "category_id"))
	private final Set<Category> categories=new HashSet<>();

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
	

	

}
