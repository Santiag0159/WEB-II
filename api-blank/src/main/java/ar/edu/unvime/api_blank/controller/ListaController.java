package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.FavoritoResponseDto;
import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.dto.ListaResponseDto;
import ar.edu.unvime.api_blank.dto.MoverFavoritosRequestDto;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Endpoints para la gestión de listas jerárquicas y reasignación de favoritos")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear lista", description = "Crea una nueva lista de favoritos recibiendo nombre y descripción.")
    public ListaResponseDto crear(@Valid @RequestBody ListaRequestDto request) {
        Lista creada = listaService.crear(new Lista(null, request.getNombre(), request.getDescripcion(), null));
        return new ListaResponseDto(creada.getId(), creada.getNombre(), creada.getDescripcion(), creada.getFechaCreacion());
    }

    @GetMapping
    @Operation(summary = "Listar listas", description = "Obtiene el listado completo de todas las listas registradas.")
    public List<ListaResponseDto> listar() {
        return listaService.obtenerTodas().stream()
                .map(l -> new ListaResponseDto(l.getId(), l.getNombre(), l.getDescripcion(), l.getFechaCreacion()))
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista", description = "Obtiene los detalles de una lista específica según su ID.")
    public ListaResponseDto obtenerPorId(@PathVariable Long id) {
        Lista lista = listaService.obtenerPorId(id);
        return new ListaResponseDto(lista.getId(), lista.getNombre(), lista.getDescripcion(), lista.getFechaCreacion());
    }

    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Favoritos de una lista", description = "Devuelve el listado de todos los favoritos contenidos dentro de la lista especificada.")
    public List<FavoritoResponseDto> obtenerFavoritos(@PathVariable Long id) {
        return listaService.obtenerFavoritosPorListaId(id).stream()
                .map(f -> new FavoritoResponseDto(f.getId(), f.getListaId(), f.getNotaPersonal(), f.getFechaAgregado()))
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una lista vacía", description = "Elimina una lista siempre y cuando no posea favoritos asociados (retorna 409 si contiene elementos).")
    public void eliminar(@PathVariable Long id) {
        listaService.eliminarPorId(id);
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mover favoritos y eliminar lista origen", description = "Reasigna todos los favoritos de la lista origen a la lista destino y elimina la lista origen dentro de una transacción atómica.")
    public void moverFavoritos(
            @PathVariable Long origenId,
            @Valid @RequestBody MoverFavoritosRequestDto request) {
        listaService.moverFavoritosYEliminarLista(origenId, request.getDestinoId());
    }
}