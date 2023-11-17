package org.kayteam.backend.apirest.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @NotEmpty
    @Size(min = 4, max = 12)
    @Column(nullable = false)
    @Getter
    @Setter
    private String nombre;
    @NotEmpty
    @Getter
    @Setter
    private String apellido;
    @NotEmpty
    @Email
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String email;
    @NotNull
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;
    private String foto;

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }

    @PreRemove
    public void preRemove(){
        Path uploads = Paths.get("uploads");
        if(getFoto() != null && !getFoto().equals("")) {
            uploads.resolve(getFoto()).toFile().delete();
        }
    }
}