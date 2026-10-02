package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.dto.ProductoDto;
import ar.edu.unvime.api_blank.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Catalogo de productos", description = "Endpoints para consultar productos consumidos desde DummyJSON")
public class ProductoController {
    private final ProductoService productoService;
    
    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }
    
    @Operation(summary = "Obtener catálogo de productos", description = "Obtiene la lista completa de productos expuestas por DummmyJSON")
    @GetMapping
    public ResponseEntity<List<ProductoDto>> obtenerTodos() {
        List<ProductoDto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener un producto por su ID", description = "Busca un producto especifico en la API externa segun su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> obtenerPorId(@PathVariable Long id) {
        ProductoDto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }
    
}
