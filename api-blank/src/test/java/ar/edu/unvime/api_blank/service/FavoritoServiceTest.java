package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.FavoritoRequestDto;
import ar.edu.unvime.api_blank.dto.FavoritoResponseDto;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
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
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritoRepository;

    @InjectMocks
    private FavoritoService favoritoService;

    @Test
    void crear_GuardaLosDatosYDevuelveRespuesta() {
        FavoritoRequestDto request = request(10L, 20L, "nota");
        LocalDateTime fecha = LocalDateTime.of(2026, 2, 3, 4, 5);
        when(favoritoRepository.guardar(any(Favorito.class))).thenAnswer(invocation -> {
            Favorito favorito = invocation.getArgument(0);
            favorito.setId(1L);
            favorito.setFechaAgregado(fecha);
            return favorito;
        });

        FavoritoResponseDto resultado = favoritoService.crear(request);

        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getProductoId());
        assertEquals("nota", resultado.getNotaPersonal());
        assertEquals(fecha, resultado.getFechaAgregado());
        verify(favoritoRepository).guardar(argThat(favorito ->
                favorito.getProductoId().equals(10L)
                        && favorito.getListaId().equals(20L)
                        && favorito.getNotaPersonal().equals("nota")));
    }

    @Test
    void obtenerTodos_MapeaResultadosDelRepositorio() {
        when(favoritoRepository.buscarTodos()).thenReturn(List.of(
                new Favorito(1L, 2L, 3L, "uno", null),
                new Favorito(4L, 5L, 6L, "dos", null)));

        List<FavoritoResponseDto> resultado = favoritoService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(5L, resultado.get(1).getProductoId());
    }

    @Test
    void obtenerPorId_MapeaCuandoExisteYConservaAusencia() {
        when(favoritoRepository.buscarPorId(1L)).thenReturn(Optional.of(
                new Favorito(1L, 2L, 3L, "nota", null)));
        when(favoritoRepository.buscarPorId(9L)).thenReturn(Optional.empty());

        assertEquals(2L, favoritoService.obtenerPorId(1L).orElseThrow().getProductoId());
        assertTrue(favoritoService.obtenerPorId(9L).isEmpty());
    }

    @Test
    void actualizar_GuardaCambiosSinModificarListaYDevuelveAusenciaSiNoExiste() {
        Favorito existente = new Favorito(1L, 2L, 3L, "anterior", null);
        when(favoritoRepository.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(favoritoRepository.buscarPorId(9L)).thenReturn(Optional.empty());
        when(favoritoRepository.guardar(any(Favorito.class))).thenAnswer(invocation -> invocation.getArgument(0));
        FavoritoRequestDto request = request(7L, 99L, "nueva");

        FavoritoResponseDto actualizado = favoritoService.actualizar(1L, request).orElseThrow();

        assertEquals(7L, actualizado.getProductoId());
        assertEquals("nueva", actualizado.getNotaPersonal());
        assertEquals(3L, existente.getListaId());
        assertTrue(favoritoService.actualizar(9L, request).isEmpty());
        verify(favoritoRepository, times(1)).guardar(existente);
    }

    @Test
    void eliminar_DevuelveSegunExistencia() {
        when(favoritoRepository.existePorId(1L)).thenReturn(true);
        when(favoritoRepository.existePorId(9L)).thenReturn(false);

        assertTrue(favoritoService.eliminar(1L));
        assertFalse(favoritoService.eliminar(9L));
        verify(favoritoRepository).eliminarPorId(1L);
        verify(favoritoRepository, never()).eliminarPorId(9L);
    }

    private FavoritoRequestDto request(Long productoId, Long listaId, String nota) {
        FavoritoRequestDto request = new FavoritoRequestDto();
        request.setProductoId(productoId);
        request.setListaId(listaId);
        request.setNotaPersonal(nota);
        return request;
    }
}
