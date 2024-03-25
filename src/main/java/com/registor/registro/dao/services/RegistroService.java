package com.registor.registro.dao.services;

import com.registor.registro.dao.IRegistroDao;
import com.registor.registro.models.model.Registro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroService implements IRegistroServicesImp{

    @Autowired
    private IRegistroDao iRegistroDao;

    @Override
    public List<Registro> findAll() {
        return this.iRegistroDao.findAll();
    }

    @Override
    public Registro save(Registro registro) {
        return this.iRegistroDao.save(registro);
    }
}
