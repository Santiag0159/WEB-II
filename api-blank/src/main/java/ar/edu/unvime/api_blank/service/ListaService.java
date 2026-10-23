package ar.edu.unvime.api_blank.service;
//Comentario para probar el ci
import ar.edu.unvime.api_blank.exception.AccesoDenegadoException;
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

    public Lista crear(Lista lista, Long usuarioAutenticadoId) {
    lista.setUsuarioId(usuarioAutenticadoId);
    return listaRepository.guardar(lista);
    }

    public List<Lista> obtenerTodas(Long usuarioAutenticadoId, String rolAutenticado) {
        if ("ROLE_ADMIN".equals(rolAutenticado)) {
            return listaRepository.buscarTodas();
        }
        return listaRepository.obtenerPorUsuarioId(usuarioAutenticadoId);
    }

    public Lista obtenerPorId(Long id, Long usuarioAutenticadoId, String rolAutenticado) {
        Lista lista = listaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lista no encontrada con ID: " + id));

        validarPropiedadOAdmin(lista, usuarioAutenticadoId, rolAutenticado);
        return lista;
    }

    public List<Favorito> obtenerFavoritosPorListaId(Long listaId, Long usuarioAutenticadoId, String rolAutenticado) {
        obtenerPorId(listaId, usuarioAutenticadoId, rolAutenticado);
        return favoritoRepositoryAdapter.buscarPorListaId(listaId);
    }

    public void eliminarPorId(Long id, Long usuarioAutenticadoId, String rolAutenticado) {
        obtenerPorId(id, usuarioAutenticadoId, rolAutenticado);
        
        try {
            listaRepository.eliminarPorId(id);
        } catch (DataIntegrityViolationException e) {
            throw new ListaConFavoritosException("No se puede eliminar la lista con ID " + id + " porque tiene favoritos asociados.");
        }
    }

    @Transactional
    public void moverFavoritosYEliminarLista(Long origenId, Long destinoId, Long usuarioAutenticadoId, String rolAutenticado) {
        obtenerPorId(origenId, usuarioAutenticadoId, rolAutenticado);
        obtenerPorId(destinoId, usuarioAutenticadoId, rolAutenticado);

        favoritoJpaRepository.reasignarFavoritosDeLista(origenId, destinoId);
        listaRepository.eliminarPorId(origenId);
    }

    public void validarPropiedadOAdmin(Lista lista, Long usuarioAutenticadoId, String rolAutenticado) {
        if ("ROLE_ADMIN".equals(rolAutenticado)) {
            return;
        }
        if (lista.getUsuarioId() == null || !lista.getUsuarioId().equals(usuarioAutenticadoId)) {
            throw new AccesoDenegadoException("No tiene permisos para acceder a esta lista");
        }
    }
}