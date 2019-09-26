package org.irods.rest.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.irods.jargon.core.utils.Base64;
import org.irods.rest.config.IrodsRestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component

@PropertySources({ @PropertySource(value = "classpath:/test.dos.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:/etc/irods-ext/ga4gh.properties", ignoreResourceNotFound = true) })

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

	@Value("${irods.host}")
	private String irodsHost;

	@Autowired(required = true)
	private IrodsRestConfiguration irodsRestConfiguration;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
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
			log.debug("built irodsRestConfiguration:{}", irodsRestConfiguration);
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
			FilterChain filterChain) {
		log.info("getAuthentication()");
		log.info("get token...");
		String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
		log.debug("token:{}", token);

		if (token == null) {
			log.warn("no token, throw auth exception");
			SecurityContextHolder.clearContext();
			return null;
		}

		if (!token.isEmpty() && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			try {
				log.info("host was set:{}", irodsHost);
				log.info("have irodsRestConfiguration:{}", irodsRestConfiguration);
				String signingKey = irodsRestConfiguration.getSharedJwtKey().trim();
				log.debug("signingkey:-{}-", signingKey);
				signingKey = Base64.toString(signingKey.getBytes());
				Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingKey)
						.parseClaimsJws(token.replace("Bearer ", "").trim());
				log.debug("parsedToken:{}", parsedToken);

				String username = parsedToken.getBody().getSubject();
				log.debug("username:{}", username);

				if (!username.isEmpty()) {
					log.info("processed claim for user:{}", username);
					List<GrantedAuthority> granted = new ArrayList<>();
					GrantedAuthority auth = new SimpleGrantedAuthority("authToken");
					granted.add(auth);
					IrodsAuthentication authToken = new IrodsAuthentication(username, "authToken", granted);
					log.info("authToken from filter:{}", authToken);
					return authToken;
				}

			} catch (ExpiredJwtException exception) {
				log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
			} catch (UnsupportedJwtException exception) {
				log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
			} catch (MalformedJwtException exception) {
				log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
			} catch (IllegalArgumentException exception) {
				log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
			}
		}

		log.warn("no auth, returning null");
		return null;
	}

	public String getIrodsHost() {
		return irodsHost;
	}

	public void setIrodsHost(String irodsHost) {
		this.irodsHost = irodsHost;
	}

	public IrodsRestConfiguration getIrodsRestConfiguration() {
		return irodsRestConfiguration;
	}

	public void setIrodsRestConfiguration(IrodsRestConfiguration irodsRestConfiguration) {
		this.irodsRestConfiguration = irodsRestConfiguration;
	}

}