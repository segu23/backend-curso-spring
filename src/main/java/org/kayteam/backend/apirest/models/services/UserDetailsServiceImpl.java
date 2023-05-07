package org.kayteam.backend.apirest.models.services;

import org.kayteam.backend.apirest.models.dao.IUsuarioDao;
import org.kayteam.backend.apirest.models.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);

        if(usuario == null) {
            throw new UsernameNotFoundException("Login Error: El usuario " + usuario + " no se encuentra en el sistema.");
        }
        return usuario;
    }
}
