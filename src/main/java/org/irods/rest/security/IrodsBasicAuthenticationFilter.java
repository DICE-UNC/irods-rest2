/**
 * 
 */
package org.irods.rest.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.connection.auth.AuthResponse;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.rest.config.IrodsRestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Validate basic auth credentials as iRODS user/password
 * 
 * @author Mike Conway - NIEHS
 *
 */

@Component

public class IrodsBasicAuthenticationFilter extends BasicAuthenticationFilter {

	private IrodsRestConfiguration irodsRestConfiguration;
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	private static final Logger log = LoggerFactory.getLogger(IrodsBasicAuthenticationFilter.class);

	public IrodsBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	public IrodsBasicAuthenticationFilter(AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationManager, authenticationEntryPoint);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		log.info("doFilterInternal()");
		if (irodsRestConfiguration == null) {
			log.debug("irodsRestConfiguration was null");
			ServletContext servletContext = request.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			irodsRestConfiguration = webApplicationContext.getBean(IrodsRestConfiguration.class);
			log.debug("built dosConfiguration:{}", irodsRestConfiguration);
		}
		if (irodsAccessObjectFactory == null) {
			log.debug("irodsAccessObjectFactory was null");
			ServletContext servletContext = request.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			irodsAccessObjectFactory = webApplicationContext.getBean(IRODSAccessObjectFactory.class);
		}

		/*
		 * see if authentication is done using a prior filter
		 */

		Authentication irodsAuthentication = SecurityContextHolder.getContext().getAuthentication();
		if (irodsAuthentication != null) {
			log.debug("authentication already done, continue");
			filterChain.doFilter(request, response);
			return;
		}

		log.debug("getting authentication");
		IrodsAuthentication authentication = getAuthentication(request, response, filterChain);
		log.debug("authentication:{}", authentication);
		if (authentication == null) {
			filterChain.doFilter(request, response);
			return;
		}

		log.info("setting auth into context:{}", authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	private IrodsAuthentication getAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException {
		log.info("getAuthentication()");
		log.info("get token...");
		String auth = request.getHeader("Authorization");
		IRODSAccount irodsAccount;
		AuthResponse authResponse = null;
		try {
			irodsAccount = RestAuthUtils.getIRODSAccountFromBasicAuthValues(auth, irodsRestConfiguration);
			log.info("irods account for auth:{}", irodsAccount);

			authResponse = irodsAccessObjectFactory.authenticateIRODSAccount(irodsAccount);
			log.info("authResponse:{}", authResponse);
			log.info("success!");
			log.info("processed claim for user:{}", irodsAccount);
			List<GrantedAuthority> granted = new ArrayList<>();
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("authToken");
			granted.add(grantedAuthority);
			IrodsAuthentication authToken = new IrodsAuthentication(irodsAccount.getUserName(), "authToken", granted);
			log.info("authToken from filter:{}", authToken);
			return authToken;
		} catch (JargonException e) {
			log.error("error getting iRODS account", e);
			throw new ServletException("error creating iRODS account", e);
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

}
