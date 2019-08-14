package org.irods.rest.api;

import org.springframework.stereotype.Controller;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-08-14T16:47:57.791Z[GMT]")
@Controller
public class FileBytesApiController implements FileBytesApi {

    private final FileBytesApiDelegate delegate;

    @org.springframework.beans.factory.annotation.Autowired
    public FileBytesApiController(FileBytesApiDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public FileBytesApiDelegate getDelegate() {
        return delegate;
    }
}
