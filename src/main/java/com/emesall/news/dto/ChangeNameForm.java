package com.emesall.news.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data

public class ChangeNameForm {

	
	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;


}
