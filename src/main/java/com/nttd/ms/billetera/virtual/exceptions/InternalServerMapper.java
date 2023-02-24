package com.nttd.ms.billetera.virtual.exceptions;

import java.time.LocalDateTime;

import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InternalServerMapper implements ExceptionMapper<Exception> {
    @Inject
    Logger logger;

    @Override
    public Response toResponse(Exception ex) {
        ExceptionResponse er = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage());

        logger.info("ERROR ::: " + ex.getStackTrace().toString());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(er)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
