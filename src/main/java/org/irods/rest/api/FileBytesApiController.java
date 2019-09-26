package org.irods.rest.api;

import org.irods.rest.api.delegates.FileBytesApiDelegateImpl;
import org.springframework.stereotype.Controller;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-18T14:35:03.245Z[GMT]")
@Controller
public class FileBytesApiController implements FileBytesApi {

	private final FileBytesApiDelegate delegate;

	@org.springframework.beans.factory.annotation.Autowired
	public FileBytesApiController(FileBytesApiDelegateImpl delegate) {
		this.delegate = delegate;
	}

	@Override
	public FileBytesApiDelegate getDelegate() {
		return delegate;
	}
}
