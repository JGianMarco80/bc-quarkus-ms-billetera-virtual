package com.nttd.ms.billetera.virtual.client;

import com.nttd.ms.billetera.virtual.client.model.BMovimiento;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/billetera-virtual-movimiento")
public interface BMovimientoClient {

    @POST
    Uni<BMovimiento> save(@QueryParam("celular") String celular,
                          @QueryParam("monto") Double monto,
                          @QueryParam("descripcion") String descripcion);

}
