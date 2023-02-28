package com.nttd.ms.billetera.virtual.service;

import com.nttd.ms.billetera.virtual.client.model.AceptarCompraBootCoin;
import com.nttd.ms.billetera.virtual.entity.Billetera;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface BilleteraService {

    //Proyecto 3
    Multi<Billetera> findAll();

    Uni<Billetera> save(Billetera billetera);

    //Uni<String> emitirPago(String id, Double monto, String celular, String descripcion);

    //Proyecto Final
    //Multi<AceptarCompraBootCoin> obtenerSolicitudesAceptadas();
    Uni<String> pfEmitirPago(String numeroTransaccion);

}
