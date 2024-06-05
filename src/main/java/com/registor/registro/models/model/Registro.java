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
@Table(name = "registrodb")
@Entity
public class Registro implements Serializable {

    @Id
    @Column(name = "id_persona")
    private String id_persona;
    @Column(name = "nombre_usuario")
    private String nombre_usuario;
    @Column(name = "email_usuario")
    private String email_usuario;
    @Column(name = "comentario_usuario")
    private String comentario_usuario;


}
