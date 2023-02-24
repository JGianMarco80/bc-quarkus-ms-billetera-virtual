package com.nttd.ms.billetera.virtual.resource;

import com.nttd.ms.billetera.virtual.entity.Billetera;
import com.nttd.ms.billetera.virtual.service.BilleteraService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/billetera-virtual")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BilleteraResource {

    @Inject
    BilleteraService billeteraService;

    @GET
    public Multi<Billetera> findAll(){
        return billeteraService.findAll();
    }

    @POST
    @Transactional
    public Uni<Billetera> save(Billetera billetera) {
        return billeteraService.save(billetera);
    }

    /*@GET
    @Path("/buscar")
    public Uni<Billetera> buscarBilleteraXCelular(@QueryParam("celular") String celular){
        return billeteraService.buscarBilleteraXCelular(celular);
    }*/

    @PUT
    @Path("/emitir-pago/{id}")
    @Transactional
    public Uni<String> emitirPago(@PathParam("id") String id,
                                  @QueryParam("monto") Double monto,
                                  @QueryParam("celular") String celular,
                                  @QueryParam("descripcion") String descripcion){
        return billeteraService.emitirPago(id, monto, celular, descripcion);
    }

}
