package ar.edu.unvime.api_blank.persistence.Usuario;

import jakarta.persistence.*;

@Entity 
@Table(name = "Usuarios")
public class UsuarioEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String rol = "ROLE_USER";

    public UsuarioEntity(){}

    public UsuarioEntity(Long id, String email, String passwordHash, String rol){
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public Long getId(){return id; }
    public String getEmail(){return email; }
    public String getPasswordHash(){return passwordHash;}
    public String getRol(){return rol;}

}
