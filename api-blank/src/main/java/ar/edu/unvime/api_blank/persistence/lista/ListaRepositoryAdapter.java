package ar.edu.unvime.api_blank.persistence.lista;

import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ListaRepositoryAdapter implements ListaRepository {

    private final ListaJpaRepository listaJpaRepository;

    public ListaRepositoryAdapter(ListaJpaRepository listaJpaRepository) {
        this.listaJpaRepository = listaJpaRepository;
    }

    @Override
    public Lista guardar(Lista lista) {
        ListaEntity entity = toEntity(lista);
        ListaEntity saved = listaJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Lista> buscarPorId(Long id) {
        return listaJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Lista> buscarTodas() {
        return listaJpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void eliminarPorId(Long id) {
        listaJpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return listaJpaRepository.existsById(id);
    }

    // Mapeos explícitos entre Modelo de Dominio y Entidad JPA
    private ListaEntity toEntity(Lista domain) {
        return new ListaEntity(
            domain.getId(),
            domain.getNombre(),
            domain.getDescripcion(),
            domain.getFechaCreacion()
        );
    }

    private Lista toDomain(ListaEntity entity) {
        return new Lista(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getFechaCreacion()
        );
    }
}