package com.nttd.ms.billetera.virtual.resource;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse.StatusCode;

import com.nttd.ms.billetera.virtual.entity.Auth;
import com.nttd.ms.billetera.virtual.service.IAuthService;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    Logger LOGGER;

    @Inject
    IAuthService service;

    @POST
    @Path("/login")
    @ResponseStatus(StatusCode.OK)
    public Uni<Boolean> login(@Valid Auth auth) {
        return this.service.login(auth);
    }

    @POST
    @Path("/sing-out")
    @ResponseStatus(StatusCode.NO_CONTENT)
    public Uni<Void> deleteById(@Valid Auth auth) {
        return this.service.signOut(auth);
    }

    @GET
    @Path("/")
    @ResponseStatus(StatusCode.OK)
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Auth> listAll() {
        return this.service.listAll();
    }
}
