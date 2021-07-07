package com.inminhouse.alone.auth.form;

import org.springframework.beans.BeanUtils;

import com.inminhouse.alone.auth.model.Auth;
import com.inminhouse.alone.auth.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
	
	private String name;
	private String imageUrl;
	private String introduce;
	private String birthday;
	private Integer gender;
	private Integer level;
	private Integer status;
	private String email;
	private String provider;
	
	public UserForm(User user, Auth auth) {
		BeanUtils.copyProperties(user, this);
		//require
		this.level = user.getLevel().getValue();
		this.status = user.getStatus().getValue();
		//null check
		this.gender = user.getGender().getValue();
		BeanUtils.copyProperties(auth, this);
		this.provider = auth.getProvider().toString();
	}
	
}
