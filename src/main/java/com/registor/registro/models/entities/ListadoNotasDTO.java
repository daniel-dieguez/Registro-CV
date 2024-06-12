package com.registor.registro.models.entities;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListadoNotasDTO implements Serializable {


    @NotEmpty(message = "debe de estar lleno el listado de notas")
    private  String notas;
}
