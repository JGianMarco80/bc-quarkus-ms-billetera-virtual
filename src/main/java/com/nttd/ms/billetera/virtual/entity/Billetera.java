package com.nttd.ms.billetera.virtual.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@MongoEntity(collection = "billetera_virtual")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billetera {

    private ObjectId id;

    //1: Natural
    //2: Juridico
    private String tipoCliente;

    private String nombreRazonSocial;

    //1: DNI
    //2: Carnet de extranjeria
    //RUC
    private String tipoDocumento;

    private String numeroDocumento;

    private String celular;

    private Double saldo;

    private String password;

    //1: No cliente del banco
    //2: Cliente del banco
    private String tipoBilletera;

    private String numeroTarjeta;

}
