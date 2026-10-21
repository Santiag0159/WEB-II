package ar.edu.unvime.api_blank.persistence.Usuario;

import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {
    private final UsuarioJpaRepository repository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity(usuario.getId(), usuario.getEmail(), usuario.getPasswordHash(), usuario.getRol());
        UsuarioEntity saved = repository.save(entity);
        return new Usuario(saved.getId(), saved.getEmail(), saved.getPasswordHash(), saved.getRol());
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(e -> new Usuario(e.getId(), e.getEmail(), e.getPasswordHash(), e.getRol()));
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }
}