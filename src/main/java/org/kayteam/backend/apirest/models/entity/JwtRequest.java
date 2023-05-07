package org.kayteam.backend.apirest.models.entity;

import lombok.Data;

@Data
public class JwtRequest {

    private String username;

    private String password;
}
