package ar.edu.unvime.api_blank.persistence.favorito;

import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.persistence.lista.ListaEntity;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpaRepository;

    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Favorito guardar(Favorito favorito) {
        FavoritoEntity entity = toEntity(favorito);
        FavoritoEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Favorito> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Favorito> buscarTodos() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    public List<Favorito> buscarPorListaId(Long listaId) {
        return jpaRepository.findByListaId(listaId).stream().map(this::toDomain).toList();
    }

    @Override
    public void eliminarPorId(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return jpaRepository.existsById(id);
    }

    private FavoritoEntity toEntity(Favorito domain) {
        LocalDateTime fecha = domain.getFechaAgregado() != null ? domain.getFechaAgregado() : LocalDateTime.now();
        ListaEntity listaEntity = new ListaEntity();
        listaEntity.setId(domain.getListaId());

        return new FavoritoEntity(
                domain.getId(),
                domain.getProductoId(),
                listaEntity,
                domain.getNotaPersonal(),
                fecha
        );
    }

    private Favorito toDomain(FavoritoEntity entity) {
        Long listaId = entity.getLista() != null ? entity.getLista().getId() : null;
        return new Favorito(
                entity.getId(),
                entity.getProductoId(),
                listaId,
                entity.getNota(),
                entity.getFechaAlta()
        );
    }
}