package org.irods.rest.api;

import javax.servlet.http.HttpServletRequest;

import org.irods.rest.api.delegates.TokenApiDelegateImpl;
import org.irods.rest.security.ContextAccountHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-18T14:35:03.245Z[GMT]")
@Controller
public class TokenApiController implements TokenApi {

	private final TokenApiDelegate delegate;

	private static final Logger log = LoggerFactory.getLogger(TokenApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	private ContextAccountHelper contextAccountHelper;

	@org.springframework.beans.factory.annotation.Autowired
	public TokenApiController(ObjectMapper objectMapper, HttpServletRequest request, TokenApiDelegateImpl delegate) {
		this.objectMapper = objectMapper;
		this.request = request;
		this.delegate = delegate;
	}

	public ResponseEntity<String> obtainToken() {
		log.info("obtainToken()");

		return delegate.obtainToken();

	}

	public ContextAccountHelper getContextAccountHelper() {
		return contextAccountHelper;
	}

	public void setContextAccountHelper(ContextAccountHelper contextAccountHelper) {
		this.contextAccountHelper = contextAccountHelper;
	}

}
