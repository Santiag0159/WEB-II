package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Lista;
import java.util.List;
import java.util.Optional;

public interface ListaRepository {
    Lista guardar(Lista lista);
    Optional<Lista> buscarPorId(Long id);
    List<Lista> buscarTodas();
    void eliminarPorId(Long id);
    boolean existePorId(Long id);
}