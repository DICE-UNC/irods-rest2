/**
 * 
 */
package org.irods.rest.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * iRODS specific authentication token that can include ticket information
 * 
 * @author Mike Conway - NIEHS
 *
 */
public class IrodsAuthentication extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = -3476986846389081094L;
	/**
	 * Optional {@code String} with an iRODS ticket string for this interaction
	 */
	private String ticket = "";

	/**
	 * @param principal   {@link Object} that is the principal
	 * @param credentials {@link Object} that is the credentials
	 */
	public IrodsAuthentication(Object principal, Object credentials) {
		super(principal, credentials);
	}

	/**
	 * @param principal   {@link Object} that is the principal
	 * @param credentials {@link Object} that is the principal
	 * @param authorities {@code List} of {@link GrantedAuthority}
	 */
	public IrodsAuthentication(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IrodsAuthentication [ticket=").append(ticket).append("]");
		return builder.toString();
	}

}
