package com.nttd.ms.billetera.virtual.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/cuenta-bancaria")
public interface CuentaBancariaClient {

    @PUT
    @Path("/actualizar-saldo")
    Uni<Double> emitirRecibirPAgo(@QueryParam("numeroTarjeta") String numeroTarjeta,
                                  @QueryParam("operacion") String operacion,
                                  @QueryParam("monto")  Double monto);

}
