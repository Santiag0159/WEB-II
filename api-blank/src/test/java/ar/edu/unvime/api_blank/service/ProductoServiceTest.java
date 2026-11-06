package ar.edu.unvime.api_blank.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ProductoServiceTest {

    @Test
    void obtenerTodos_MapeaProductos() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://dummyjson.com/products"))
                .andRespond(withSuccess("""
                        {"products":[{"id":1,"title":"title","description":"description",
                        "price":2.5,"category":"category"}]}
                        """, MediaType.APPLICATION_JSON));
        ProductoService service = new ProductoService(builder);

        var productos = service.obtenerTodos();

        assertEquals(1, productos.size());
        assertEquals(1L, productos.get(0).getId());
        assertEquals("title", productos.get(0).getNombre());
        assertEquals("description", productos.get(0).getDescripcion());
        assertEquals(2.5, productos.get(0).getPrecio());
        assertEquals("category", productos.get(0).getCategoria());
        server.verify();
    }

    @Test
    void obtenerTodos_DevuelveListaVaciaSiLaRespuestaONoTieneProductos() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://dummyjson.com/products"))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        server.expect(requestTo("https://dummyjson.com/products"))
                .andRespond(withSuccess("{\"products\":null}", MediaType.APPLICATION_JSON));
        ProductoService service = new ProductoService(builder);

        assertTrue(service.obtenerTodos().isEmpty());
        assertTrue(service.obtenerTodos().isEmpty());
        server.verify();
    }

    @Test
    void obtenerPorId_MapeaProductoYDevuelveNuloSiNoHayCuerpo() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://dummyjson.com/products/4"))
                .andRespond(withSuccess("""
                        {"id":4,"title":"title","description":"description",
                        "price":9.5,"category":"category"}
                        """, MediaType.APPLICATION_JSON));
        server.expect(requestTo("https://dummyjson.com/products/5"))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        ProductoService service = new ProductoService(builder);

        assertEquals("title", service.obtenerPorId(4L).getNombre());
        assertNull(service.obtenerPorId(5L));
        server.verify();
    }
}
