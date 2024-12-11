package com.sparta.kidscafe.domain.user.entity;

import com.sparta.kidscafe.common.enums.LoginType;

public interface OAuthMember {
	Long getId();

	String getEmail();

	LoginType getLoginType();

	String getOauthId();
}
