/**
 * 
 */
package org.irods.rest.config;

import org.irods.jargon.core.connection.AuthScheme;
import org.irods.rest.exception.IrodsRestRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

/*
 * 
 * @author Mike Conway - NIEHS
 *
 */

@PropertySources({ @PropertySource(value = "classpath:/test.rest.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:/etc/irods-ext/rest.properties", ignoreResourceNotFound = true) })

@Component
public class IrodsRestConfiguration {

	@Value("${irods.host}")
	private String irodsHost;

	@Value("${irods.zone}")
	private String irodsZone;

	@Value("${default.storage.resource}")
	private String irodsDefaultResource;

	@Value("${proxy.user}")
	private String proxyUser;

	@Value("${proxy.password}")
	private String proxyPassword;

	@Value("${shared.jwt.key}")
	private String sharedJwtKey;

	@Value("${jwt.algo}")
	private String jwtAlgo;

	@Value("${irods.port}")
	private int port;

	@Value("${irods.realm}")
	private String realm;

	@Value("${auth.type}")
	private String authScheme;

	@Value("${ssl.negotiation.policy}")
	private String sslNegotiationPolicy;

	@Value("${irodsext.datatyper.persist.data.types}")
	private boolean persistDataTypes;

	@Value("${irodsext.datatyper.detailed.determination}")
	private boolean detailedDataTypeDetermination;

	public AuthScheme translateAuthSchemeToEnum() {
		String authSchemeStr = authScheme;
		if (authSchemeStr == null || authSchemeStr.isEmpty()) {
			return AuthScheme.STANDARD;
		} else if (authSchemeStr.equals(AuthScheme.STANDARD.toString())) {
			return AuthScheme.STANDARD;
		} else if (authSchemeStr.equals(AuthScheme.PAM.toString())) {
			return AuthScheme.PAM;
		} else {
			throw new IrodsRestRuntimeException("unknown authscheme");
		}
	}

	public String getIrodsHost() {
		return irodsHost;
	}

	public void setIrodsHost(String irodsHost) {
		this.irodsHost = irodsHost;
	}

	public String getIrodsZone() {
		return irodsZone;
	}

	public void setIrodsZone(String irodsZone) {
		this.irodsZone = irodsZone;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getAuthScheme() {
		return authScheme;
	}

	public void setAuthScheme(String authScheme) {
		this.authScheme = authScheme;
	}

	public boolean isPersistDataTypes() {
		return persistDataTypes;
	}

	public void setPersistDataTypes(boolean persistDataTypes) {
		this.persistDataTypes = persistDataTypes;
	}

	public boolean isDetailedDataTypeDetermination() {
		return detailedDataTypeDetermination;
	}

	public void setDetailedDataTypeDetermination(boolean detailedDataTypeDetermination) {
		this.detailedDataTypeDetermination = detailedDataTypeDetermination;
	}

	public void setSslNegotiationPolicy(String sslNegotiationPolicy) {
		this.sslNegotiationPolicy = sslNegotiationPolicy;
	}

	public String getSslNegotiationPolicy() {
		return sslNegotiationPolicy;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public String getSharedJwtKey() {
		return sharedJwtKey;
	}

	public void setSharedJwtKey(String sharedJwtKey) {
		this.sharedJwtKey = sharedJwtKey;
	}

	public String getJwtAlgo() {
		return jwtAlgo;
	}

	public void setJwtAlgo(String jwtAlgo) {
		this.jwtAlgo = jwtAlgo;
	}

	public String getIrodsDefaultResource() {
		return irodsDefaultResource;
	}

	public void setIrodsDefaultResource(String irodsDefaultResource) {
		this.irodsDefaultResource = irodsDefaultResource;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IrodsRestConfiguration [irodsHost=").append(irodsHost).append(", irodsZone=").append(irodsZone)
				.append(", irodsDefaultResource=").append(irodsDefaultResource).append(", proxyUser=").append(proxyUser)
				.append(", sharedJwtKey=").append(sharedJwtKey).append(", jwtAlgo=").append(jwtAlgo).append(", port=")
				.append(port).append(", realm=").append(realm).append(", authScheme=").append(authScheme)
				.append(", sslNegotiationPolicy=").append(sslNegotiationPolicy).append(", persistDataTypes=")
				.append(persistDataTypes).append(", detailedDataTypeDetermination=")
				.append(detailedDataTypeDetermination).append("]");
		return builder.toString();
	}

}
