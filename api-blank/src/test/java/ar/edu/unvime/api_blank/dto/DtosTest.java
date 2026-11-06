package ar.edu.unvime.api_blank.dto;

import ar.edu.unvime.api_blank.model.Favorito;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtosTest {

    @Test
    void favoritoRequestDto_LeeYEscribeSusCampos() {
        FavoritoRequestDto dto = new FavoritoRequestDto();
        dto.setProductoId(11L);
        dto.setListaId(12L);
        dto.setNotaPersonal("para probar");

        assertEquals(11L, dto.getProductoId());
        assertEquals(12L, dto.getListaId());
        assertEquals("para probar", dto.getNotaPersonal());
    }

    @Test
    void favoritoResponseDto_MapeaEntidadYPermiteActualizarCampos() {
        LocalDateTime fecha = LocalDateTime.of(2026, 1, 2, 3, 4);
        Favorito favorito = new Favorito(1L, 2L, 3L, "nota", fecha);

        FavoritoResponseDto dto = FavoritoResponseDto.desdeEntidad(favorito);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getProductoId());
        assertEquals("nota", dto.getNotaPersonal());
        assertEquals(fecha, dto.getFechaAgregado());

        dto.setId(4L);
        dto.setProductoId(5L);
        dto.setNotaPersonal("actualizada");
        dto.setFechaAgregado(fecha.plusDays(1));
        assertEquals(4L, dto.getId());
        assertEquals(5L, dto.getProductoId());
        assertEquals("actualizada", dto.getNotaPersonal());
        assertEquals(fecha.plusDays(1), dto.getFechaAgregado());
    }

    @Test
    void dummyJsonDtos_LeeYEscribeCamposDeProducto() {
        DummyProductDto producto = new DummyProductDto();
        producto.setId(1L);
        producto.setTitle("title");
        producto.setDescription("description");
        producto.setPrice(2.5);
        producto.setCategory("category");

        assertEquals(1L, producto.getId());
        assertEquals("title", producto.getTitle());
        assertEquals("description", producto.getDescription());
        assertEquals(2.5, producto.getPrice());
        assertEquals("category", producto.getCategory());

        DummyJsonResponseDto respuesta = new DummyJsonResponseDto();
        respuesta.setProducts(List.of(producto));
        assertEquals(List.of(producto), respuesta.getProducts());
    }

    @Test
    void productoDto_ConstructorYSettersConservanDatos() {
        ProductoDto dto = new ProductoDto(1L, "nombre", "descripcion", 4.5, "categoria");
        assertEquals(1L, dto.getId());
        assertEquals("nombre", dto.getNombre());
        assertEquals("descripcion", dto.getDescripcion());
        assertEquals(4.5, dto.getPrecio());
        assertEquals("categoria", dto.getCategoria());

        dto.setId(2L);
        dto.setNombre("otro");
        dto.setDescripcion("otra descripcion");
        dto.setPrecio(7.5);
        dto.setCategoria("otra categoria");
        assertEquals(2L, dto.getId());
        assertEquals("otro", dto.getNombre());
        assertEquals("otra descripcion", dto.getDescripcion());
        assertEquals(7.5, dto.getPrecio());
        assertEquals("otra categoria", dto.getCategoria());
    }

    @Test
    void moverFavoritosRequestDto_UsaConstructorYSetter() {
        MoverFavoritosRequestDto dto = new MoverFavoritosRequestDto(1L);
        assertEquals(1L, dto.getDestinoId());

        dto.setDestinoId(2L);
        assertEquals(2L, dto.getDestinoId());
    }
}
