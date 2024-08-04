package com.registor.registro.models.model.upFile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "modalbase")
public class FileEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "data", length = 1000000)
    private byte[] data;

    @Column(name = "nombre_completo")
    private String nombre_Completo;

    @Column(name = "comentario_user")
    private String comentario_user;


    public FileEntity(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
