package org.kayteam.backend.apirest.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Column(unique = true, length = 20)
    @Getter
    @Setter
    private String nombre;
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
}
