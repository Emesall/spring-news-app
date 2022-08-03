package com.emesall.news.mapper;

import org.springframework.stereotype.Component;

import com.emesall.news.dto.ChangeNameForm;
import com.emesall.news.model.User;

@Component
public class ChangeNameFormMapper {

	public ChangeNameForm userToChangeNameForm(User user) {

		ChangeNameForm nameForm=new ChangeNameForm();
		nameForm.setFirstName(user.getFirstName());
		nameForm.setLastName(user.getLastName());

		return nameForm;
	}

}
