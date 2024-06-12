package com.registor.registro.models.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "listadonotas")
@Entity
public class ListadoNota implements Serializable {

    @Id
    @Column(name = "id_listado")
    private String id_listado;
    @Column(name = "notas")
    private String notas;
}
