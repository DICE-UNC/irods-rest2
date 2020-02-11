/**
 * 
 */
package org.irods.rest.security;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.rest.config.IrodsRestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mike Conway - NIEHS
 *
 */
@Component
public class ContextAccountHelper {
	@Autowired
	private IrodsRestConfiguration irodsRestConfiguration;

	private static final Logger log = LoggerFactory.getLogger(ContextAccountHelper.class);

	/**
	 * Build an account from an authentication that can be found in the
	 * {@link SecurityContext}
	 * 
	 * @param userName {@code String} with user name
	 * @return {@link IRODSAccount}
	 * @throws JargonException {@link JargonException}
	 */
	public IRODSAccount irodsAccountFromAuthentication(final String userName) throws JargonException {
		log.info("irodsAccountFromAuthentication()");
		if (userName == null) {
			throw new IllegalArgumentException("Null authentication");
		}

		if (userName == IRODSAccount.PUBLIC_USERNAME) {
			log.info("using anonymous public login");
			IRODSAccount irodsAccount = IRODSAccount.instanceForAnonymous(irodsRestConfiguration.getIrodsHost(),
					irodsRestConfiguration.getPort(), "", irodsRestConfiguration.getIrodsZone(), "");
			log.debug("formulated iRODS account from auth:{}", irodsAccount);
			return irodsAccount;
		} else {
			IRODSAccount irodsAccount = IRODSAccount.instanceWithProxy(irodsRestConfiguration.getIrodsHost(),
					irodsRestConfiguration.getPort(), userName, irodsRestConfiguration.getProxyPassword(), "",
					irodsRestConfiguration.getIrodsZone(), "", irodsRestConfiguration.getProxyUser(), "");

			log.debug("formulated iRODS account from auth:{}", irodsAccount);
			return irodsAccount;
		}
	}

	public IrodsRestConfiguration getDosConfiguration() {
		return irodsRestConfiguration;
	}

	public void setDosConfiguration(IrodsRestConfiguration dosConfiguration) {
		this.irodsRestConfiguration = dosConfiguration;
	}

}
