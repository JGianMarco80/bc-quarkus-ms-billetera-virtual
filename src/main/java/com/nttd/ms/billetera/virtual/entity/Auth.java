package com.nttd.ms.billetera.virtual.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth {

    @NotEmpty(message = "El campo celular es requerido.")
    private String celular;

    @NotEmpty(message = "El campo password es requerido.")
    private String password;

}
