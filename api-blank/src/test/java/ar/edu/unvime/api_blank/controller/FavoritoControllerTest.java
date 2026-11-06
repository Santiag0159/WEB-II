package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.FavoritoRequestDto;
import ar.edu.unvime.api_blank.dto.FavoritoResponseDto;
import ar.edu.unvime.api_blank.service.FavoritoService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FavoritoControllerTest {

    private final StubFavoritoService favoritoService = new StubFavoritoService();
    private final FavoritoController controller = new FavoritoController(favoritoService);

    @Test
    void crearYObtenerTodos_DevuelvenRespuestasExitosas() {
        FavoritoResponseDto response = new FavoritoResponseDto();
        favoritoService.creado = response;
        favoritoService.todos = List.of(response);

        assertEquals(HttpStatus.CREATED, controller.crear(new FavoritoRequestDto()).getStatusCode());
        assertEquals(HttpStatus.OK, controller.obtenerTodos().getStatusCode());
        assertEquals(1, controller.obtenerTodos().getBody().size());
    }

    @Test
    void obtenerPorId_DevuelveEncontradoONotFound() {
        FavoritoResponseDto response = new FavoritoResponseDto();
        favoritoService.porId.put(1L, Optional.of(response));
        favoritoService.porId.put(9L, Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerPorId(9L).getStatusCode());
    }

    @Test
    void actualizar_DevuelveEncontradoONotFound() {
        FavoritoResponseDto response = new FavoritoResponseDto();
        FavoritoRequestDto request = new FavoritoRequestDto();
        favoritoService.actualizados.put(1L, Optional.of(response));
        favoritoService.actualizados.put(9L, Optional.empty());

        assertEquals(HttpStatus.OK, controller.actualizar(1L, request).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.actualizar(9L, request).getStatusCode());
    }

    @Test
    void eliminar_DevuelveNoContentONotFound() {
        favoritoService.eliminados.put(1L, true);
        favoritoService.eliminados.put(9L, false);

        assertEquals(HttpStatus.NO_CONTENT, controller.eliminar(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.eliminar(9L).getStatusCode());
    }

    private static class StubFavoritoService extends FavoritoService {
        private FavoritoResponseDto creado;
        private List<FavoritoResponseDto> todos = List.of();
        private final java.util.Map<Long, Optional<FavoritoResponseDto>> porId = new java.util.HashMap<>();
        private final java.util.Map<Long, Optional<FavoritoResponseDto>> actualizados = new java.util.HashMap<>();
        private final java.util.Map<Long, Boolean> eliminados = new java.util.HashMap<>();

        private StubFavoritoService() {
            super(null);
        }

        @Override
        public FavoritoResponseDto crear(FavoritoRequestDto requestDto) {
            return creado;
        }

        @Override
        public List<FavoritoResponseDto> obtenerTodos() {
            return todos;
        }

        @Override
        public Optional<FavoritoResponseDto> obtenerPorId(Long id) {
            return porId.getOrDefault(id, Optional.empty());
        }

        @Override
        public Optional<FavoritoResponseDto> actualizar(Long id, FavoritoRequestDto requestDto) {
            return actualizados.getOrDefault(id, Optional.empty());
        }

        @Override
        public boolean eliminar(Long id) {
            return eliminados.getOrDefault(id, false);
        }
    }
}
