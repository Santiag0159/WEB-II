package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.exception.AccesoDenegadoException;
import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existePorEmail(usuario.getEmail())) {
            throw new AccesoDenegadoException("El email ya se encuentra registrado");
        }

        String contraseniaEncriptada = passwordEncoder.encode(usuario.getPasswordHash());
        usuario.setPasswordHash(contraseniaEncriptada);

        return usuarioRepository.guardar(usuario);
    }
}