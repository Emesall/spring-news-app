package com.emesall.news.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.emesall.news.validation.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches(field = "password", fieldMatch = "matchingPassword")
public class RegistrationForm {

	
	@Email(regexp = ".+@.+\\..+")
    private String email;

	@NotBlank
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$")
	private String password;

	@NotBlank
	private String matchingPassword;

	private String firstName;

	
	private String lastName;


}
