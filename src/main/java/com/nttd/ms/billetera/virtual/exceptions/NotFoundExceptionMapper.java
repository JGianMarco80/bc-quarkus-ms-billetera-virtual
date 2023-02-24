package com.nttd.ms.billetera.virtual.exceptions;

import java.time.LocalDateTime;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException ex) {
        ExceptionResponse er = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage());

        return Response.status(Response.Status.NOT_FOUND).entity(er)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}