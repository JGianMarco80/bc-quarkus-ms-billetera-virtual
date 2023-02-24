package com.nttd.ms.billetera.virtual.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestModelExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        ExceptionResponse er = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getConstraintViolations().stream()
                        .map(cv -> cv.getMessage())
                        .collect(Collectors.joining(", ")));

        return Response.status(Response.Status.BAD_REQUEST).entity(er)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
