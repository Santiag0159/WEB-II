package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.FavoritoResponseDto;
import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.dto.ListaResponseDto;
import ar.edu.unvime.api_blank.dto.MoverFavoritosRequestDto;
import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Gestión de listas y reasignación de favoritos. Todos los endpoints requieren JWT Bearer; cada usuario accede a sus listas y ADMIN puede acceder a todas.")
@SecurityRequirement(name = "bearerAuth")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear lista")
    public ListaResponseDto crear(@Valid @RequestBody ListaRequestDto request, Authentication authentication) {
        Long usuarioId = obtenerId(authentication);
        Lista nuevaLista = new Lista(null, request.getNombre(), request.getDescripcion(), null, null);
        
        Lista creada = listaService.crear(nuevaLista, usuarioId);
        
        return toDto(creada);
    }

    @GetMapping
    @Operation(summary = "Listar listas")
    public List<ListaResponseDto> listar(Authentication authentication) {
        Long usuarioId = obtenerId(authentication);
        String rol = obtenerRol(authentication);
        
        return listaService.obtenerTodas(usuarioId, rol).stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista")
    public ListaResponseDto obtenerPorId(@PathVariable Long id, Authentication authentication) {
        Long usuarioId = obtenerId(authentication);
        String rol = obtenerRol(authentication);
        
        Lista lista = listaService.obtenerPorId(id, usuarioId, rol);
        return toDto(lista);
    }

    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Favoritos de una lista")
    public List<FavoritoResponseDto> obtenerFavoritos(@PathVariable Long id, Authentication authentication) {
        Long usuarioId = obtenerId(authentication);
        String rol = obtenerRol(authentication);
        
        return listaService.obtenerFavoritosPorListaId(id, usuarioId, rol).stream()
                .map(f -> new FavoritoResponseDto(f.getId(), f.getListaId(), f.getNotaPersonal(), f.getFechaAgregado()))
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una lista vacía")
    public void eliminar(@PathVariable Long id, Authentication authentication) {
        Long usuarioId = obtenerId(authentication);
        String rol = obtenerRol(authentication);
        
        listaService.eliminarPorId(id, usuarioId, rol);
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mover favoritos y eliminar lista origen")
    public void moverFavoritos(
            @PathVariable Long origenId,
            @Valid @RequestBody MoverFavoritosRequestDto request,
            Authentication authentication) {
        
        Long usuarioId = obtenerId(authentication);
        String rol = obtenerRol(authentication);
        
        listaService.moverFavoritosYEliminarLista(origenId, request.getDestinoId(), usuarioId, rol);
    }

    // Metodos auxiliares
    private Long obtenerId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    private String obtenerRol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
    }

    private ListaResponseDto toDto(Lista lista) {
        return new ListaResponseDto(lista.getId(), lista.getNombre(), lista.getDescripcion(), lista.getFechaCreacion());
    }
}