package org.kayteam.backend.apirest.models.dao;

import org.kayteam.backend.apirest.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    Usuario findByUsername(String username);
}
