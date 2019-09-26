/**
 * 
 */
package org.irods.rest.config;

import org.irods.jargon.core.connection.ClientServerNegotiationPolicy;
import org.irods.jargon.core.connection.ClientServerNegotiationPolicy.SslNegotiationPolicy;
import org.irods.jargon.core.connection.IRODSSession;
import org.irods.jargon.core.connection.SettableJargonProperties;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Mike Conway Wired-in class that takes configuration and core jargon
 *         components and injects appropriate configuration into the underlying
 *         jargon properties system
 *
 */
public class StartupConfigurator {

	private IRODSSession irodsSession;
	private IRODSAccessObjectFactory irodsAccessObjectFactory;
	private IrodsRestConfiguration irodsRestConfiguration;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public StartupConfigurator() {

	}

	/**
	 * @return the irodsSession
	 */
	public IRODSSession getIrodsSession() {
		return irodsSession;
	}

	/**
	 * @param irodsSession the irodsSession to set
	 */
	public void setIrodsSession(IRODSSession irodsSession) {
		this.irodsSession = irodsSession;
	}

	/**
	 * this method is wired into the spring config after the injection of the props
	 * and <code>IRODSSession</code> so that property configuration can be
	 * accomplished
	 */
	public void init() {
		log.info("init()");

		if (irodsSession == null) {
			log.error("null irodsSession");
			throw new IllegalStateException("null irodsSession");
		}

		SettableJargonProperties props = new SettableJargonProperties(irodsSession.getJargonProperties());
		// props.setComputeChecksumAfterTransfer(dosConfiguration.isComputeChecksum());
		// log.info("set checksum policy to:{}", dosConfiguration.isComputeChecksum());

		SslNegotiationPolicy policyToSet = ClientServerNegotiationPolicy
				.findSslNegotiationPolicyFromString(irodsRestConfiguration.getSslNegotiationPolicy());

		log.info("policyToSet:{}", policyToSet);

		props.setNegotiationPolicy(policyToSet);
		log.info("negotiation policy set to:{}", props.getNegotiationPolicy());

		getIrodsSession().setJargonProperties(props);
		log.info("config of jargon props complete");

	}

	/**
	 * @return the irodsAccessObjectFactory
	 */
	public IRODSAccessObjectFactory getIrodsAccessObjectFactory() {
		return irodsAccessObjectFactory;
	}

	/**
	 * @param irodsAccessObjectFactory the irodsAccessObjectFactory to set
	 */
	public void setIrodsAccessObjectFactory(IRODSAccessObjectFactory irodsAccessObjectFactory) {
		this.irodsAccessObjectFactory = irodsAccessObjectFactory;
	}

	public IrodsRestConfiguration getIrodsRestConfiguration() {
		return irodsRestConfiguration;
	}

	public void setIrodsRestConfiguration(IrodsRestConfiguration irodsRestConfiguration) {
		this.irodsRestConfiguration = irodsRestConfiguration;
	}

}
