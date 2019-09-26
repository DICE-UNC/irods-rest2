package org.irods.rest.api;

import org.irods.rest.api.delegates.TokenApiDelegateImpl;
import org.springframework.stereotype.Controller;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-18T14:35:03.245Z[GMT]")
@Controller
public class TokenApiController implements TokenApi {

	private final TokenApiDelegate delegate;

	@org.springframework.beans.factory.annotation.Autowired
	public TokenApiController(TokenApiDelegateImpl delegate) {
		this.delegate = delegate;
	}

	@Override
	public TokenApiDelegate getDelegate() {
		return delegate;
	}
}
