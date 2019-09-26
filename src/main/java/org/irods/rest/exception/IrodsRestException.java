/**
 * 
 */
package org.irods.rest.exception;

import org.irods.jargon.core.exception.JargonException;

/**
 * @author Mike Conway - NIEHS
 *
 */
public class IrodsRestException extends JargonException {

	private static final long serialVersionUID = 589377422541808969L;

	public IrodsRestException(String message) {
		super(message);
	}

	public IrodsRestException(String message, Throwable cause) {
		super(message, cause);
	}

	public IrodsRestException(Throwable cause) {
		super(cause);
	}

	public IrodsRestException(String message, Throwable cause, int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

	public IrodsRestException(Throwable cause, int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	public IrodsRestException(String message, int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

}
