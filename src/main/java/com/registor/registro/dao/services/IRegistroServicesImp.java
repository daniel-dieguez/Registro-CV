package com.registor.registro.dao.services;

import com.registor.registro.models.model.Registro;

import java.util.List;

public interface IRegistroServicesImp {

    public List<Registro> findAll();
    public Registro save (Registro registro);
}
