package ar.edu.unvime.api_blank.repository;

import ar.edu.unvime.api_blank.model.Favorito;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository {
    Favorito guardar(Favorito favorito);
    List<Favorito> buscarTodos();
    Optional<Favorito> buscarPorId(Long id);
    boolean existePorId(Long id);
    void eliminarPorId(Long id);
}