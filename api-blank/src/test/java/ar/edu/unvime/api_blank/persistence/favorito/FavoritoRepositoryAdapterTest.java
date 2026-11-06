package ar.edu.unvime.api_blank.persistence.favorito;

import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.persistence.lista.ListaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoRepositoryAdapterTest {

    @Mock
    private FavoritoJpaRepository jpaRepository;

    @InjectMocks
    private FavoritoRepositoryAdapter adapter;

    @Test
    void guardar_UsaFechaActualCuandoElDominioNoTieneFecha() {
        when(jpaRepository.save(any(FavoritoEntity.class))).thenAnswer(invocation -> {
            FavoritoEntity entity = invocation.getArgument(0);
            entity.setId(5L);
            return entity;
        });

        Favorito resultado = adapter.guardar(new Favorito(null, 10L, 20L, "nota", null));

        assertEquals(5L, resultado.getId());
        assertEquals(10L, resultado.getProductoId());
        assertEquals(20L, resultado.getListaId());
        assertEquals("nota", resultado.getNotaPersonal());
        assertNotNull(resultado.getFechaAgregado());
        verify(jpaRepository).save(argThat(entity ->
                entity.getLista().getId().equals(20L) && entity.getFechaAlta() != null));
    }

    @Test
    void guardar_ConservaFechaExistente() {
        LocalDateTime fecha = LocalDateTime.of(2025, 6, 7, 8, 9);
        when(jpaRepository.save(any(FavoritoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Favorito resultado = adapter.guardar(new Favorito(1L, 2L, 3L, "nota", fecha));

        assertEquals(fecha, resultado.getFechaAgregado());
        verify(jpaRepository).save(argThat(entity -> fecha.equals(entity.getFechaAlta())));
    }

    @Test
    void buscarPorId_MapeaEntidadYDevuelveVacio() {
        FavoritoEntity entity = entity(1L, 2L, 3L, "nota");
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jpaRepository.findById(9L)).thenReturn(Optional.empty());

        Favorito resultado = adapter.buscarPorId(1L).orElseThrow();

        assertEquals(1L, resultado.getId());
        assertEquals(2L, resultado.getProductoId());
        assertEquals(3L, resultado.getListaId());
        assertEquals("nota", resultado.getNotaPersonal());
        assertTrue(adapter.buscarPorId(9L).isEmpty());
    }

    @Test
    void buscarTodosYPorLista_MapeanResultados() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity(1L, 2L, 3L, "a")));
        when(jpaRepository.findByListaId(3L)).thenReturn(List.of(entity(4L, 5L, 3L, "b")));

        assertEquals(1L, adapter.buscarTodos().get(0).getId());
        assertEquals(4L, adapter.buscarPorListaId(3L).get(0).getId());
    }

    @Test
    void eliminarYExiste_DeleganAlRepositorioJpa() {
        when(jpaRepository.existsById(1L)).thenReturn(true);

        assertTrue(adapter.existePorId(1L));
        adapter.eliminarPorId(1L);

        verify(jpaRepository).existsById(1L);
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    void buscarPorId_EntidadSinListaProduceListaIdNulo() {
        FavoritoEntity entity = new FavoritoEntity(1L, 2L, null, "nota", null);
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertNull(adapter.buscarPorId(1L).orElseThrow().getListaId());
    }

    private FavoritoEntity entity(Long id, Long productoId, Long listaId, String nota) {
        ListaEntity lista = new ListaEntity();
        lista.setId(listaId);
        return new FavoritoEntity(id, productoId, lista, nota, LocalDateTime.now());
    }
}
