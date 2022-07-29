package com.emesall.news.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.emesall.news.validation.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches(field = "password", fieldMatch = "matchingPassword")
public class RegistrationForm {

	
	@Email(regexp = ".+@.+\\..+")
    private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String matchingPassword;

	private String firstName;

	
	private String lastName;


}
