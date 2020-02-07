package org.irods.rest.api;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.ticket.TicketClientSupport;
import org.irods.rest.exception.IrodsRestRuntimeException;
import org.irods.rest.security.ContextAccountHelper;
import org.irods.rest.security.IrodsAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-18T14:35:03.245Z[GMT]")
@Controller
public class FileBytesApiController implements FileBytesApi {
	private static final Logger log = LoggerFactory.getLogger(FileBytesApiController.class);

	@Autowired
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	@Autowired
	private ContextAccountHelper contextAccountHelper;

	public FileBytesApiController() {
	}

	@Override
	public void fileBytes(String path, Integer offset, Integer limit, HttpServletResponse response) {

		log.info("fileBytes()");
		IrodsAuthentication irodsAuthentication = (IrodsAuthentication) SecurityContextHolder.getContext()
				.getAuthentication();
		log.debug("authentication is: {}", irodsAuthentication);
		try {
			IRODSAccount irodsAccount = contextAccountHelper
					.irodsAccountFromAuthentication(irodsAuthentication.getName());
			log.info("irodsAccount:{}", irodsAccount);

			if (!irodsAuthentication.getTicket().isEmpty()) {
				// use TicketClientSupport
				TicketClientSupport ticketClientSupport = new TicketClientSupport(irodsAccessObjectFactory,
						irodsAccount);
				ticketClientSupport.initializeSessionWithTicket(irodsAuthentication.getTicket());
			}

			IRODSFile irodsFile = this.irodsAccessObjectFactory.getIRODSFileFactory(irodsAccount)
					.instanceIRODSFile(path);

			if (!irodsFile.exists()) {
				log.info("file does not exist");
				throw new IrodsRestRuntimeException("file not found");
			}

			InputStream input = new org.irods.jargon.core.pub.io.PackingIrodsInputStream(
					irodsAccessObjectFactory.getIRODSFileFactory(irodsAccount).instanceIRODSFileInputStream(irodsFile));

			log.debug("************* all response headers ************");

			int contentLength = (int) irodsFile.length();

			response.setContentType("application/octet-stream");
			response.setContentLength(contentLength);
			response.setHeader("Content-disposition", "attachment; filename=\"" + irodsFile.getName() + "\"");

			OutputStream output;

			output = new BufferedOutputStream(response.getOutputStream());

			org.irods.jargon.core.pub.Stream2StreamAO stream2StreamAO = irodsAccessObjectFactory
					.getStream2StreamAO(irodsAccount);
			stream2StreamAO.streamToStreamCopyUsingStandardIO(input, output);
		} catch (IOException ioe) {
			log.error("io exception getting output stream to download file", ioe);
			throw new IrodsRestRuntimeException("exception downloading iRODS data", ioe);
		} catch (JargonException e) {
			log.error("exception getting output stream to download file", e);
			throw new IrodsRestRuntimeException("exception downloading iRODS data", e);
		} finally {
			irodsAccessObjectFactory.closeSessionAndEatExceptions();

		}

	}
}
