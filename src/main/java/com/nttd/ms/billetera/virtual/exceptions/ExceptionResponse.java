package com.nttd.ms.billetera.virtual.exceptions;

import java.time.LocalDateTime;

public class ExceptionResponse {

    private LocalDateTime fecha;
    private String mensaje;

    public ExceptionResponse(LocalDateTime fecha, String mensaje) {
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
