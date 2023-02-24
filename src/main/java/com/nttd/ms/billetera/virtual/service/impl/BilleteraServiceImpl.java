package com.nttd.ms.billetera.virtual.service.impl;

import com.nttd.ms.billetera.virtual.client.BMovimientoClient;
import com.nttd.ms.billetera.virtual.client.CuentaBancariaClient;
import com.nttd.ms.billetera.virtual.client.model.BMovimiento;
import com.nttd.ms.billetera.virtual.entity.Billetera;
import com.nttd.ms.billetera.virtual.repository.BilleteraRepository;
import com.nttd.ms.billetera.virtual.service.BilleteraService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class BilleteraServiceImpl implements BilleteraService {

    @Inject
    BilleteraRepository billeteraRepository;

    @RestClient
    CuentaBancariaClient cuentaBancariaClient;

    @RestClient
    BMovimientoClient bMovimientoClient;

    @Override
    public Multi<Billetera> findAll() {
        return billeteraRepository.listAll()
                .onItem()
                .<Billetera>disjoint()
                .map(billetera -> billetera);
    }

    @Override
    public Uni<Billetera> save(Billetera billetera) {
        return billeteraRepository.persist(billetera);
    }

    /*@Override
    public Uni<String> emitirPago(String id, Double monto, String celular) {
        return this.buscarBilleteraXCelular(celular)
                .onItem()
                .transform(billetera -> {
                    billetera.setSaldo(billetera.getSaldo() + monto);
                    return billetera;
                })
                .call(billetera -> billeteraRepository.update(billetera))
                .call(() -> this.buscarBilleteraXId(id)
                        .onItem()
                        .transform( billetera -> {
                            if (billetera.getSaldo() > monto) {
                                billetera.setSaldo(billetera.getSaldo() - monto);
                            }
                            return billetera;
                        })
                        .call(billetera -> billeteraRepository.update(billetera)) )
                .onItem()
                .transformToUni(b -> Uni.createFrom().item("Se emitió el pago al siguiente numero " + celular));
    }*/

    @Override
    public Uni<String> emitirPago(String id, Double monto, String celular) {
        return this.buscarBilleteraXCelular(celular)
                .onItem()
                .transform(billetera -> {
                    billetera.setSaldo(billetera.getSaldo() + monto);
                    if (billetera.getTipoBilletera().equals("2")) {
                        cuentaBancariaClient.emitirRecibirPAgo(billetera.getNumeroTarjeta(), "1", monto)
                                .subscribe().asCompletionStage();
                    }
                    return billetera;
                })
                .call(billetera -> billeteraRepository.update(billetera))
                .call(billetera -> bMovimientoClient.save(billetera.getCelular(), monto, "Para el chaufa"))
                .call(() -> this.buscarBilleteraXId(id)
                        .onItem()
                        .transform( billetera -> {
                            if (billetera.getSaldo() > monto) {
                                billetera.setSaldo(billetera.getSaldo() - monto);
                                if (billetera.getTipoBilletera().equals("2")) {
                                    cuentaBancariaClient.emitirRecibirPAgo(billetera.getNumeroTarjeta(), "2", monto)
                                            .subscribe().asCompletionStage();
                                }
                            }
                            return billetera;
                        })
                        .call(billetera -> billeteraRepository.update(billetera))
                        .call(billetera -> bMovimientoClient.save(billetera.getCelular(), (monto * -1), "Para el chaufa")))
                .onItem()
                .transformToUni(b -> Uni.createFrom().item("Se emitió el pago al siguiente numero " + celular));
    }

    public Uni<Billetera> buscarBilleteraXCelular(String celular) {
        return this.findAll()
                .select()
                .when(billetera -> Uni.createFrom().item( billetera.getCelular().equals(celular)))
                .toUni()
                .onItem()
                .ifNull()
                .failWith(() -> new NotFoundException("No existe una billetera con el numero " + celular));
    }

    public Uni<Billetera> buscarBilleteraXId(String id) {
        return billeteraRepository.findById(new ObjectId(id))
                .onItem()
                .ifNull()
                .failWith(() -> new NotFoundException("No existe una billetera con el id " + id));
    }

    /*private Uni<Billetera> buscarbilleteraXDocumento(String numeroDocumento) {
        return this.findAll()
                .select()
                .when(billetera -> Uni.createFrom().item( billetera.getCelular().equals(numeroDocumento)) )
                //.invoke(Unchecked.consumer(i -> {
                    //throw new IOException("boom");
                //}))
                .toUni();
    }*/
}
