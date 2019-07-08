package com.yakimov.teamdocs.utils;

public class SecurityConstraints {
	public static Long EXPIRATION_TIME = 864_000_000L; // 10 days
	public static final String SECRET = "SekREtKKEEEY";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
}
