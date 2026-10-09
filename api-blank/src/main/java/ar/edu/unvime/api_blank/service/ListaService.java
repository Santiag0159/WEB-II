package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.exception.ListaConFavoritosException;
import ar.edu.unvime.api_blank.exception.RecursoNoEncontradoException;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.persistence.favorito.FavoritoJpaRepository;
import ar.edu.unvime.api_blank.persistence.favorito.FavoritoRepositoryAdapter;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListaService {

    private final ListaRepository listaRepository;
    private final FavoritoRepositoryAdapter favoritoRepositoryAdapter;
    private final FavoritoJpaRepository favoritoJpaRepository;

    public ListaService(ListaRepository listaRepository,
                        FavoritoRepositoryAdapter favoritoRepositoryAdapter,
                        FavoritoJpaRepository favoritoJpaRepository) {
        this.listaRepository = listaRepository;
        this.favoritoRepositoryAdapter = favoritoRepositoryAdapter;
        this.favoritoJpaRepository = favoritoJpaRepository;
    }

    public Lista crear(Lista lista) {
        return listaRepository.guardar(lista);
    }

    public List<Lista> obtenerTodas() {
        return listaRepository.buscarTodas();
    }

    public Lista obtenerPorId(Long id) {
        return listaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lista no encontrada con ID: " + id));
    }

    public List<Favorito> obtenerFavoritosPorListaId(Long listaId) {
        if (!listaRepository.existePorId(listaId)) {
            throw new RecursoNoEncontradoException("Lista no encontrada con ID: " + listaId);
        }
        return favoritoRepositoryAdapter.buscarPorListaId(listaId);
    }

    public void eliminarPorId(Long id) {
        if (!listaRepository.existePorId(id)) {
            throw new RecursoNoEncontradoException("Lista no encontrada con ID: " + id);
        }
        try {
            listaRepository.eliminarPorId(id);
        } catch (DataIntegrityViolationException e) {
            throw new ListaConFavoritosException("No se puede eliminar la lista con ID " + id + " porque tiene favoritos asociados.");
        }
    }

    @Transactional
    public void moverFavoritosYEliminarLista(Long origenId, Long destinoId) {
        if (!listaRepository.existePorId(origenId)) {
            throw new RecursoNoEncontradoException("Lista origen no encontrada con ID: " + origenId);
        }
        if (!listaRepository.existePorId(destinoId)) {
            throw new RecursoNoEncontradoException("Lista destino no encontrada con ID: " + destinoId);
        }

        favoritoJpaRepository.reasignarFavoritosDeLista(origenId, destinoId);
        listaRepository.eliminarPorId(origenId);
    }
}