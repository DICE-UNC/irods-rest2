package org.irods.rest.exception;

import org.irods.jargon.core.exception.JargonRuntimeException;

public class IrodsRestRuntimeException extends JargonRuntimeException {

	private static final long serialVersionUID = 7804311100622076164L;

	public IrodsRestRuntimeException() {
	}

	public IrodsRestRuntimeException(String message) {
		super(message);
	}

	public IrodsRestRuntimeException(Throwable cause) {
		super(cause);
	}

	public IrodsRestRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
