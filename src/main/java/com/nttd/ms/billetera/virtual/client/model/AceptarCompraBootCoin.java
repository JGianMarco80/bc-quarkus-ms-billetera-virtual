package com.nttd.ms.billetera.virtual.client.model;

import lombok.Data;

@Data
public class AceptarCompraBootCoin {

    private Double monto;

    //1: billetera digital
    //2: transferencia
    private String modoPago;

    private String numeroTransaccion;

    //Comprador
    //1: celular
    //2: numero de cuenta
    private String cTipoNumero;

    private String cNumero;

    //Vendedor
    //1: celular
    //2: numero de cuenta
    private String vTipoNumero;

    private String vNumero;
    
}
