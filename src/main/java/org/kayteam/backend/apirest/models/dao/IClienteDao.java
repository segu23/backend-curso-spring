package org.kayteam.backend.apirest.models.dao;

import org.kayteam.backend.apirest.models.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

}
