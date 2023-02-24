package com.nttd.ms.billetera.virtual.service;

import com.nttd.ms.billetera.virtual.entity.Billetera;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface BilleteraService {

    Multi<Billetera> findAll();

    Uni<Billetera> save(Billetera billetera);

    Uni<String> emitirPago(String id, Double monto, String celular);

}
