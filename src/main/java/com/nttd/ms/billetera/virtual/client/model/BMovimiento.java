package com.nttd.ms.billetera.virtual.client.model;

import lombok.Data;
import org.bson.types.ObjectId;
import java.time.LocalDate;

@Data
public class BMovimiento {

    private ObjectId id;

    private String celular;

    private LocalDate fecha;

    private Double monto;

    private String descripcion;

}
