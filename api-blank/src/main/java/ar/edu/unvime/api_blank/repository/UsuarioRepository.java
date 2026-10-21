package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEmail(String email);
}
