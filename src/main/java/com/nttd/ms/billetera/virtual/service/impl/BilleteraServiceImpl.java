package com.nttd.ms.billetera.virtual.service.impl;

import com.nttd.ms.billetera.virtual.client.BMovimientoClient;
import com.nttd.ms.billetera.virtual.client.CuentaBancariaClient;
import com.nttd.ms.billetera.virtual.client.MonederoClient;
import com.nttd.ms.billetera.virtual.client.model.AceptarCompraBootCoin;
import com.nttd.ms.billetera.virtual.client.model.BMovimiento;
import com.nttd.ms.billetera.virtual.entity.Billetera;
import com.nttd.ms.billetera.virtual.repository.BilleteraRepository;
import com.nttd.ms.billetera.virtual.service.BilleteraService;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
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

    @Inject
    ReactiveRedisDataSource redisDataSource;

    @RestClient
    MonederoClient monederoClient;

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

    //Proyecto3
    /*@Override
    public Uni<String> emitirPago(String id, Double monto, String celular, String descripcion) {
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
                .call(billetera -> bMovimientoClient.save(billetera.getCelular(), monto, descripcion, billetera.getNombreRazonSocial()))
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
                        .call(billetera -> bMovimientoClient.save(billetera.getCelular(), (monto * -1), descripcion, billetera.getNombreRazonSocial())))
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
    }*/

    /*private Uni<Billetera> buscarbilleteraXDocumento(String numeroDocumento) {
        return this.findAll()
                .select()
                .when(billetera -> Uni.createFrom().item( billetera.getCelular().equals(numeroDocumento)) )
                //.invoke(Unchecked.consumer(i -> {
                    //throw new IOException("boom");
                //}))
                .toUni();
    }*/

    //Proyecto Final
    @Override
    public Uni<String> pfEmitirPago(String numeroTransaccion) {
        return this.obtenerSolicitudesAceptadas()
                .select()
                .when( solicitud -> Uni.createFrom().item( solicitud.getNumeroTransaccion().equals(numeroTransaccion)) )
                .toUni()
                .onItem()
                .ifNull()
                .failWith(() -> new BadRequestException("Numero de transacción incorrecto."))
                .onItem()
                .transform(acb -> {
                    if( acb.getVTipoNumero().equals("1") ) {
                        this.buscarBilleteraXCelular(acb.getVNumero())
                                .onItem()
                                .transform(billetera -> {
                                    billetera.setSaldo(billetera.getSaldo() + (acb.getMonto() * 3.82));
                                    if (billetera.getTipoBilletera().equals("2")) {
                                        cuentaBancariaClient.emitirRecibirPAgo(billetera.getNumeroTarjeta(), "1", (acb.getMonto() * 3.82))
                                                .subscribe().asCompletionStage();
                                    }
                                    return billetera;
                                })
                                .call(billetera -> billeteraRepository.update(billetera))
                                .subscribe().asCompletionStage();
                    }
                    if( acb.getVTipoNumero().equals("2") ) {
                        cuentaBancariaClient.emitirRecibirPagoCB(acb.getVNumero(), "1", (acb.getMonto() * 3.82))
                                .subscribe().asCompletionStage();
                    }
                    return acb;
                })
                .onItem()
                .transform(ac -> {
                    if( ac.getCTipoNumero().equals("1") ){
                        this.buscarBilleteraXCelular(ac.getCNumero())
                                .onItem()
                                .transform(billetera -> {
                                    if (billetera.getSaldo() > (ac.getMonto() * 3.78)) {
                                        billetera.setSaldo(billetera.getSaldo() - (ac.getMonto() * 3.78));
                                        if (billetera.getTipoBilletera().equals("2")) {
                                            cuentaBancariaClient.emitirRecibirPAgo(billetera.getNumeroTarjeta(), "2", (ac.getMonto() * 3.78))
                                                    .subscribe().asCompletionStage();
                                        }
                                    } else{
                                        throw new NotFoundException("No cuenta con suficiente dinero en su billetera.");
                                    }
                                    return billetera;
                                })
                                .call(billetera -> billeteraRepository.update(billetera))
                                .subscribe().asCompletionStage();
                    }
                    if( ac.getCTipoNumero().equals("2") ) {
                        cuentaBancariaClient.emitirRecibirPagoCB(ac.getCNumero(), "2", (ac.getMonto() * 3.82))
                                .subscribe().asCompletionStage();
                    }
                    return ac;
                })
                .onItem()
                .transformToUni(b -> Uni.createFrom().item("Se realizo el pago del bootCoin"));
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

    //Redis numero de transación
    private Multi<AceptarCompraBootCoin> obtenerSolicitudesAceptadas() {
        return this.redisDataSource.key().keys("aceptar-solicitud: " + "*")
                .onItem()
                .<String>disjoint()
                .flatMap(key -> this.obtenerSolicitudAceptada(key).toMulti());
    }

    private Uni<AceptarCompraBootCoin> obtenerSolicitudAceptada(String key) {
        return this.redisDataSource
                .value(String.class, AceptarCompraBootCoin.class)
                .get(key);
    }
}
