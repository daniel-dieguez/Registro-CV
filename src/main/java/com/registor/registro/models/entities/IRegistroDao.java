package com.registor.registro.models.entities;

import com.registor.registro.models.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRegistroDao extends JpaRepository<Registro, Object> {

}
