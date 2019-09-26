/**
 * 
 */
package org.irods.rest.api.delegates;

import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.rest.api.FileBytesApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mike Conway - NIEHS
 *
 */
@Component
public class FileBytesApiDelegateImpl implements FileBytesApiDelegate {

	@Autowired
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	public FileBytesApiDelegateImpl() {
	}

	public IRODSAccessObjectFactory getIrodsAccessObjectFactory() {
		return irodsAccessObjectFactory;
	}

	public void setIrodsAccessObjectFactory(IRODSAccessObjectFactory irodsAccessObjectFactory) {
		this.irodsAccessObjectFactory = irodsAccessObjectFactory;
	}

}
