package com.sparta.kidscafe.common.dto;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.entity.User;

import lombok.Getter;

@Getter
public class AuthUser {

	private final Long id;
	private final String email;
	private final RoleType roleType;

	public AuthUser(Long id, String email, RoleType roleType) {
		this.id = id;
		this.email = email;
		this.roleType = roleType;
	}

	public User toUser() {
		return User.builder()
			.id(this.id)
			.email(this.email)
			.role(this.roleType)
			.build();
	}
}
