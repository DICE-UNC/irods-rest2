package org.irods.rest.api;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A delegate to be called by the {@link TokenApiController}}. Implement this
 * interface with a {@link org.springframework.stereotype.Service} annotated
 * class.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-18T14:35:03.245Z[GMT]")
public interface TokenApiDelegate {

	Logger log = LoggerFactory.getLogger(TokenApi.class);

	ObjectMapper getObjectMapper();

	default Optional<HttpServletRequest> getRequest() {
		return Optional.empty();
	}

	default Optional<String> getAcceptHeader() {
		return getRequest().map(r -> r.getHeader("Accept"));
	}

	/**
	 * @see TokenApi#obtainToken
	 */
	ResponseEntity<String> obtainToken();
}
