/**
 * 
 */
package org.irods.rest.config;

import org.irods.jargon.core.connection.IRODSProtocolManager;
import org.irods.jargon.core.connection.IRODSSession;
import org.irods.jargon.core.connection.IRODSSimpleProtocolManager;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSAccessObjectFactoryImpl;
import org.irods.jargon.extensions.datatyper.DataTypeResolutionServiceFactory;
import org.irods.jargon.extensions.datatyper.DataTyperSettings;
import org.irodsext.datatyper.IrodsextDataTypeResolutionServiceFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Additional beans needed by boot app
 * 
 * @author Mike Conway - NIEHS
 *
 */
@Configuration
public class IrodsRestAdditionalBeanConfiguration {

	public IrodsRestAdditionalBeanConfiguration() {
	}

	/**
	 * Setting used by data typer (may refactor later). This is using the simplest
	 * defaults
	 * 
	 * @return {@link DataTyperSettings}
	 */
	@Bean
	public DataTyperSettings dataTyperSettings() {
		DataTyperSettings dataTyperSettings = new DataTyperSettings();
		dataTyperSettings.setDetailedDetermination(false);
		dataTyperSettings.setPersistDataTypes(false);
		return dataTyperSettings;
	}

	/**
	 * Factory for an object to obtain MIME types for data
	 * 
	 * @param dataTyperSettings        {@link DataTyperSettings}
	 * @param irodsAccessObjectFactory {@link IRODSAccessObjectFactory}
	 * @return
	 */
	@Bean
	public DataTypeResolutionServiceFactory dataTyperResolutionServiceFactory(DataTyperSettings dataTyperSettings,
			IRODSAccessObjectFactory irodsAccessObjectFactory) {
		IrodsextDataTypeResolutionServiceFactoryImpl dataTypeResolutionServiceFactory = new IrodsextDataTypeResolutionServiceFactoryImpl();
		dataTypeResolutionServiceFactory.setDataTyperSettings(dataTyperSettings);
		dataTypeResolutionServiceFactory.setIrodsAccessObjectFactory(irodsAccessObjectFactory);
		return dataTypeResolutionServiceFactory;
	}

	/**
	 * Source of iRODS connections
	 * 
	 * @return {@link IRODSProtocolManager}
	 */
	@Bean
	public IRODSProtocolManager irodsProtocolManager() {
		IRODSProtocolManager irodsProtocolManager = new IRODSSimpleProtocolManager();
		try {
			irodsProtocolManager.initialize();
		} catch (JargonException e) {
			throw new IllegalStateException(e);
		}
		return irodsProtocolManager;
	}

	/**
	 * Main jargon session
	 * 
	 * @param irodsProtocolManager {@link IRODSProtocolManager}
	 * @return {@link IRODSSession}
	 */
	@Bean
	public IRODSSession irodsSession(IRODSProtocolManager irodsProtocolManager) {
		IRODSSession irodsSession;
		try {
			irodsSession = IRODSSession.instance(irodsProtocolManager);
		} catch (JargonException e) {
			throw new IllegalStateException(e);
		}
		return irodsSession;
	}

	/**
	 * Factory for Jargon objects and Files
	 * 
	 * @param irodsSession {@link IRODSSession}
	 * @return {@link IRODSAccessObjectFactory}
	 */
	@Bean
	public IRODSAccessObjectFactory irodsAccessObjectFactory(IRODSSession irodsSession) {
		IRODSAccessObjectFactory irodsAccessObjectFactory = new IRODSAccessObjectFactoryImpl(irodsSession);
		return irodsAccessObjectFactory;
	}

	/**
	 * Code to update Jargon properties from provided configuration
	 * 
	 * @param irodsSession             {@link IRODSSession}
	 * @param irodsAccessObjectFactory {@link IRODSAccessObjectFactory}
	 * @param irodsRestConfiguration   {@link IrodsRestConfiguration}
	 * @return {@link StartupConfigurator}
	 */
	@Bean
	public StartupConfigurator startupConfigurator(IRODSSession irodsSession,
			IRODSAccessObjectFactory irodsAccessObjectFactory, IrodsRestConfiguration irodsRestConfiguration) {
		StartupConfigurator startupConfigurator = new StartupConfigurator();
		startupConfigurator.setIrodsRestConfiguration(irodsRestConfiguration);
		startupConfigurator.setIrodsAccessObjectFactory(irodsAccessObjectFactory);
		startupConfigurator.setIrodsSession(irodsSession);
		startupConfigurator.init();
		return startupConfigurator;

	}

}
