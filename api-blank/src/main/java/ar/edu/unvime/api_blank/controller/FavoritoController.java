package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.FavoritoRequestDto;
import ar.edu.unvime.api_blank.dto.FavoritoResponseDto;
import ar.edu.unvime.api_blank.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List; 

@RestController 
@RequestMapping("/api/favorito")
@Tag(name="Gestion de favoritos", description = "CRUD completo de la lista de favoritos en memoria RAM")
public class FavoritoController{
    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService){
        this.favoritoService = favoritoService;
    }

    @Operation(summary = "Crear un nuevo favorito", description = "Guarda un producto como favorito registrando la nota personal y la fecha actual")
    @PostMapping 
    public ResponseEntity<FavoritoResponseDto> crear(@Valid @RequestBody FavoritoRequestDto requestDto){
        FavoritoResponseDto creado = favoritoService.crear(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Listar todos los favoritos", description = "Devuelve la lista completa de favoritos almacenados en memoria")
    @GetMapping
    public ResponseEntity<List<FavoritoResponseDto>> obtenerTodos(){
        List<FavoritoResponseDto> favoritos = favoritoService.obtenerTodos();
        return ResponseEntity.ok(favoritos);
    }

    @Operation(summary = "Obtener favorito por ID", description = "Recupera los detalles de un favorito especifico almacenado por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<FavoritoResponseDto> obtenerPorId(@PathVariable Long id){
        return favoritoService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar favorito existente", description = "Actualiza los datos (Nota personal o Producto) de un favorito existente")
    @PutMapping("/{id}")
    public ResponseEntity<FavoritoResponseDto> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody FavoritoRequestDto requestDto){
            return favoritoService.actualizar(id, requestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        }

    @Operation(summary = "Eliminar favorito por ID", description = "Remueve permanentemente un favorito de la coleccion en memoria")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        boolean eliminado = favoritoService.eliminar(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.noContent().build();
    }
}