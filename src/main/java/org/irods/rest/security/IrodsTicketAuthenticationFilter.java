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

import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.rest.config.IrodsRestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Validate tickets as auth tokens
 * 
 * @author Mike Conway - NIEHS
 *
 */

public class IrodsTicketAuthenticationFilter extends BasicAuthenticationFilter {

	private IrodsRestConfiguration irodsRestConfiguration;
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	private static final Logger log = LoggerFactory.getLogger(IrodsTicketAuthenticationFilter.class);

	public IrodsTicketAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	public IrodsTicketAuthenticationFilter(AuthenticationManager authenticationManager,
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

		// see if there is a ticket string...

		String ticket = request.getHeader(SecurityConstants.TICKET_HEADER);
		if (ticket == null || ticket.isEmpty()) {
			log.info("no ticket, just keep going");
			filterChain.doFilter(request, response);
			return;
		}

		log.info("have a ticket, do I have a prior auth");

		/*
		 * see if authentication is done using a prior filter
		 */

		IrodsAuthentication irodsAuthentication = (IrodsAuthentication) SecurityContextHolder.getContext()
				.getAuthentication();
		if (irodsAuthentication != null) {
			log.debug("authentication already done, add ticket and continue");
			irodsAuthentication.setTicket(ticket);
			filterChain.doFilter(request, response);
			return;
		}

		/*
		 * With a ticket, auth as public (this will def need adjustment)
		 * 
		 * TODO: add some guardrails here
		 */
		List<GrantedAuthority> granted = new ArrayList<>();
		GrantedAuthority auth = new SimpleGrantedAuthority("authToken");
		granted.add(auth);
		IrodsAuthentication authToken = new IrodsAuthentication("anonymous", "authToken", granted); // FIXME: add
																									// explicit
																									// ticket user
		authToken.setTicket(ticket);
		SecurityContextHolder.getContext().setAuthentication(authToken);

		log.info("setting auth into context:{}", authToken);
		filterChain.doFilter(request, response);
	}

	public IrodsRestConfiguration getIrodsRestConfiguration() {
		return irodsRestConfiguration;
	}

	public void setIrodsRestConfiguration(IrodsRestConfiguration irodsRestConfiguration) {
		this.irodsRestConfiguration = irodsRestConfiguration;
	}

}
