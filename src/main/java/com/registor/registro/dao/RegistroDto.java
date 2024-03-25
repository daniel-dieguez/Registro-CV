package com.registor.registro.dao;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistroDto  implements Serializable {

    @NotEmpty(message = "Este campo debe de estar lleno ")
    private String registro;
}
