package com.nttd.ms.billetera.virtual.client;

import com.nttd.ms.billetera.virtual.client.model.AceptarCompraBootCoin;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/monedero")
public interface MonederoClient {

    @GET
    @Path("/solicitudes-aceptadas-bootCoin")
    Multi<AceptarCompraBootCoin> obtenerSolicitudesAceptadas();

}
