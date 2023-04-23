package org.kayteam.backend.apirest.models.services;

import org.kayteam.backend.apirest.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    List<Cliente> findAll();

    Page<Cliente> findAll(Pageable pageable);

    Cliente save(Cliente cliente);

    void delete(Long id);

    Cliente findById(Long id);
}
