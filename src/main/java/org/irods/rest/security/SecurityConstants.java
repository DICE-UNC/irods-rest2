package org.irods.rest.security;

public class SecurityConstants {

	// JWT token defaults
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String TICKET_HEADER = "X-API-KEY";
	public static final String TOKEN_TYPE = "JWT";
	public static final String TOKEN_ISSUER = "irods-rest";
	public static final String TOKEN_AUDIENCE = "ga4gh-dos";

}
