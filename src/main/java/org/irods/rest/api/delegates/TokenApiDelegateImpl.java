/**
 * 
 */
package org.irods.rest.api.delegates;

import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.rest.api.TokenApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * delegate to provide token api functions
 * 
 * @author Mike Conway - NIEHS
 *
 */
@Component
public class TokenApiDelegateImpl implements TokenApiDelegate {

	@Autowired
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

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

}
