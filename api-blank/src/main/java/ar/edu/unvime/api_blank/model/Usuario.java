package ar.edu.unvime.api_blank.model;

public class Usuario {
    private Long id;
    private String email;
    private String passwordHash;
    private String rol;


    public Usuario(Long id, String email, String passwordHash, String rol){
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    //Getters 
    public Long getId(){return id;}
    public String getEmail(){return email;}
    public String getPasswordHash(){return passwordHash;}
    public String getRol(){return rol;}

}
