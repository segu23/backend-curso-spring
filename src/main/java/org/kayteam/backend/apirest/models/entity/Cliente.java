package org.kayteam.backend.apirest.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    @Setter
    private Date createAt;
    @Getter
    @Setter
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