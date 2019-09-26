/**
 * 
 */
package org.irods.rest.security;

import org.apache.commons.codec.binary.Base64;
import org.irods.jargon.core.connection.AuthScheme;
import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.rest.config.IrodsRestConfiguration;
import org.irods.rest.exception.IrodsRestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class RestAuthUtils {

	private static Logger log = LoggerFactory.getLogger(RestAuthUtils.class);

	public static String basicAuthTokenFromIRODSAccount(final IRODSAccount irodsAccount) {
		if (irodsAccount == null) {
			throw new IllegalArgumentException("null irodsAccount");
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Basic ");

		StringBuilder toEncode = new StringBuilder();
		toEncode.append(irodsAccount.getUserName());
		toEncode.append(":");
		toEncode.append(irodsAccount.getPassword());

		sb.append(Base64.encodeBase64String(toEncode.toString().getBytes()));
		return sb.toString();
	}

	/**
	 * Given the raw 'basic' auth header (with the Basic prefix), build an iRODS
	 * account
	 * 
	 * @param basicAuthData     {@link String}
	 * @param restConfiguration {@link IrodsRestConfiguration}
	 * @return {@link IRODSAccount}
	 * @throws JargonException {@link JargonException}
	 */
	public static IRODSAccount getIRODSAccountFromBasicAuthValues(final String basicAuthData,
			final IrodsRestConfiguration restConfiguration) throws JargonException {

		log.info("getIRODSAccountFromBasicAuthValues");

		if (basicAuthData == null || basicAuthData.isEmpty()) {
			throw new IllegalArgumentException("null or empty basicAuthData");
		}

		if (restConfiguration == null) {
			throw new IllegalArgumentException("null restConfiguration");
		}

		final int index = basicAuthData.indexOf(' ');
		log.info("index of end of basic prefix:{}", index);
		String auth = basicAuthData.substring(index);

		String decoded = new String(Base64.decodeBase64(auth));

		log.info("index of end of basic prefix:{}", index);
		if (decoded.isEmpty()) {
			throw new JargonException("user and password not in credentials");

		}
		final String[] credentials = decoded.split(":");

		log.info("credentials:{}", credentials);

		if (credentials.length != 2) {
			throw new JargonException("user and password not in credentials");
		}

		log.info("restConfiguration:{}", restConfiguration);

		AuthScheme authScheme;
		if (restConfiguration.getAuthScheme() == null || restConfiguration.getAuthScheme().isEmpty()) {
			log.info("unspecified authType, use STANDARD");
			authScheme = AuthScheme.STANDARD;
		} else if (restConfiguration.getAuthScheme().equals(AuthScheme.STANDARD.toString())) {
			log.info("using standard auth");
			authScheme = AuthScheme.STANDARD;
		} else if (restConfiguration.getAuthScheme().equals(AuthScheme.PAM.toString())) {
			log.info("using PAM");
			authScheme = AuthScheme.PAM;
		} else {
			log.error("cannot support authScheme:{}", restConfiguration);
			throw new IrodsRestException("unknown or unsupported auth scheme");
		}

		log.info("see if auth scheme is overrideen by the provided credentials");
		/*
		 * Ids can be prepended with STANDARD: or PAM:
		 */

		String userId = credentials[0];
		if (userId.startsWith(AuthScheme.STANDARD.toString())) {
			log.info("authScheme override to Standard");
			authScheme = AuthScheme.STANDARD;
			userId = userId.substring(AuthScheme.STANDARD.toString().length() + 1);
		} else if (userId.startsWith(AuthScheme.PAM.toString())) {
			log.info("authScheme override to PAM");
			authScheme = AuthScheme.PAM;
			userId = userId.substring(AuthScheme.PAM.toString().length() + 1);
		}

		log.debug("userId:{}", userId);

		return IRODSAccount.instance(restConfiguration.getIrodsHost(), restConfiguration.getPort(), userId,
				credentials[1], "", restConfiguration.getIrodsZone(), restConfiguration.getIrodsDefaultResource(),
				authScheme);

	}

	/**
	 * Create an <code>IRODSAccount</code> suitable for anonymous access.
	 *
	 * @param restConfiguration {@link IrodsRestConfiguration}
	 * @return <code>IRODSAccount</code> suitable for anonymous access
	 * @throws JargonException
	 */
	public static IRODSAccount instanceForAnonymous(final IrodsRestConfiguration restConfiguration)
			throws JargonException {

		if (restConfiguration == null) {
			throw new IllegalArgumentException("null restConfiguration");
		}

		return IRODSAccount.instance(restConfiguration.getIrodsHost(), restConfiguration.getPort(),
				IRODSAccount.PUBLIC_USERNAME, "anonymous", "", restConfiguration.getIrodsZone(),
				restConfiguration.getIrodsDefaultResource());
	}

}
