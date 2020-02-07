/**
 * 
 */
package org.irods.rest.api.delegates;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.irodsext.jwt.AbstractJwtIssueService;
import org.irods.jargon.irodsext.jwt.JwtIssueServiceImpl;
import org.irods.jargon.irodsext.jwt.JwtServiceConfig;
import org.irods.rest.api.TokenApi;
import org.irods.rest.api.TokenApiDelegate;
import org.irods.rest.config.IrodsRestConfiguration;
import org.irods.rest.security.ContextAccountHelper;
import org.irods.rest.security.IrodsAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * delegate to provide token api functions
 * 
 * @author Mike Conway - NIEHS
 *
 */
@Service
public class TokenApiDelegateImpl implements TokenApiDelegate {

	@Autowired
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	@Autowired
	private IrodsRestConfiguration irodsRestConfiguration;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private ContextAccountHelper contextAccountHelper;

	public static final Logger log = LoggerFactory.getLogger(TokenApiDelegateImpl.class);

	/**
	 * 
	 */
	public TokenApiDelegateImpl() {
	}

	public IRODSAccessObjectFactory getIrodsAccessObjectFactory() {
		return irodsAccessObjectFactory;
	}

	public void setIrodsAccessObjectFactory(IRODSAccessObjectFactory irodsAccessObjectFactory) {
		this.irodsAccessObjectFactory = irodsAccessObjectFactory;
	}

	/**
	 * @see TokenApi#obtainToken
	 */
	public ResponseEntity<String> obtainToken() {

		log.info("obtainToken()");
		IrodsAuthentication irodsAuthentication = (IrodsAuthentication) SecurityContextHolder.getContext()
				.getAuthentication();
		log.debug("authentication is: {}", irodsAuthentication);

		IRODSAccount irodsAccount = contextAccountHelper.irodsAccountFromAuthentication(irodsAuthentication.getName());
		log.info("irodsAccount:{}", irodsAccount);

		try {
			JwtServiceConfig jwtServiceConfig = new JwtServiceConfig();
			jwtServiceConfig.setAlgo(irodsRestConfiguration.getJwtAlgo());
			jwtServiceConfig.setIssuer("irods-rest2");
			jwtServiceConfig.setSecret(irodsRestConfiguration.getSharedJwtKey());
			AbstractJwtIssueService jwtIssueService = new JwtIssueServiceImpl(jwtServiceConfig);

			log.info("user is authenticated, a JWT will be issued");
			String jwt = jwtIssueService.issueJwtToken(irodsAccount.getUserName());
			return new ResponseEntity<String>(jwt, HttpStatus.OK);

		} finally {
			irodsAccessObjectFactory.closeSessionAndEatExceptions();
		}
	}

	public IrodsRestConfiguration getIrodsRestConfiguration() {
		return irodsRestConfiguration;
	}

	public void setIrodsRestConfiguration(IrodsRestConfiguration irodsRestConfiguration) {
		this.irodsRestConfiguration = irodsRestConfiguration;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public ContextAccountHelper getContextAccountHelper() {
		return contextAccountHelper;
	}

	public void setContextAccountHelper(ContextAccountHelper contextAccountHelper) {
		this.contextAccountHelper = contextAccountHelper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

}
