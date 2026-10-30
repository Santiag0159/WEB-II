package ar.edu.unvime.api_blank.integration;

import ar.edu.unvime.api_blank.dto.ListaResponseDto;
import ar.edu.unvime.api_blank.dto.ListaRequestDto;
import ar.edu.unvime.api_blank.dto.LoginRequestDto;
import ar.edu.unvime.api_blank.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ListasSecurityIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String tokenUsuario1;
    private String tokenUsuario2;
    private String tokenAdmin;
    private Long listaUsuario1Id;

    @BeforeEach
    void prepararEntorno() {
        jdbcTemplate.execute("DELETE FROM favoritos");
        jdbcTemplate.execute("DELETE FROM listas");
        jdbcTemplate.execute("DELETE FROM usuarios");

        restTemplate.postForEntity("/api/auth/registro", new LoginRequestDto("user1@test.com", "123"), Void.class);
        restTemplate.postForEntity("/api/auth/registro", new LoginRequestDto("user2@test.com", "123"), Void.class);
        restTemplate.postForEntity("/api/auth/registro", new LoginRequestDto("admin@test.com", "123"), Void.class);

        jdbcTemplate.update("UPDATE usuarios SET rol = 'ROLE_ADMIN' WHERE email = 'admin@test.com'");

        tokenUsuario1 = obtenerToken("user1@test.com", "123");
        tokenUsuario2 = obtenerToken("user2@test.com", "123");
        tokenAdmin = obtenerToken("admin@test.com", "123");

        HttpEntity<ListaRequestDto> peticionCrear = new HttpEntity<>(new ListaRequestDto("Lista U1", "Desc"), crearHeaders(tokenUsuario1));
        
        ResponseEntity<ListaResponseDto> resCrear = restTemplate.postForEntity("/api/listas", peticionCrear, ListaResponseDto.class);
        assertEquals(HttpStatus.CREATED, resCrear.getStatusCode());
        assertNotNull(resCrear.getBody());
        listaUsuario1Id = resCrear.getBody().getId();
        assertNotNull(listaUsuario1Id);
    }

    private String obtenerToken(String email, String password) {
        ResponseEntity<AuthController.TokenResponse> res = restTemplate.postForEntity(
                "/api/auth/login", new LoginRequestDto(email, password), AuthController.TokenResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertNotNull(res.getBody().token());
        return res.getBody().token();
    }

    private HttpHeaders crearHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    void login_ConCredencialesValidas_Retorna200YToken() {
        LoginRequestDto peticion = new LoginRequestDto("user1@test.com", "123");
        ResponseEntity<String> respuesta = restTemplate.postForEntity("/api/auth/login", peticion, String.class);
        
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
    }

    @Test
    void login_ConCredencialesInvalidas_Retorna401() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> peticion = new HttpEntity<>("{\"email\":\"user1@test.com\",\"password\":\"mal\"}", headers);
            
            ResponseEntity<String> respuesta = restTemplate.exchange("/api/auth/login", HttpMethod.POST, peticion, String.class);
            assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
        } catch (ResourceAccessException e) {
            assertTrue(e.getMessage().contains("streaming mode") || e.getMessage().contains("401"));
        }
    }

    @Test
    void accederEndpointProtegido_SinToken_Retorna401() {
        ResponseEntity<String> respuesta = restTemplate.getForEntity("/api/listas", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
    }

    @Test
    void accederEndpointProtegido_ConTokenInvalido_Retorna401() {
        HttpEntity<Void> peticion = new HttpEntity<>(crearHeaders("token-invalido"));
        ResponseEntity<String> respuesta = restTemplate.exchange("/api/listas", HttpMethod.GET, peticion, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
    }

    @Test
    void consultarDocumentacionSwagger_SinToken_Retorna200() {
        ResponseEntity<String> respuesta = restTemplate.getForEntity("/v3/api-docs", String.class);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }

    @Test
    void accederListaPropia_Retorna200() {
        HttpEntity<Void> peticionGet = new HttpEntity<>(crearHeaders(tokenUsuario1));
        ResponseEntity<String> respuesta = restTemplate.exchange("/api/listas/" + listaUsuario1Id, HttpMethod.GET, peticionGet, String.class);
        
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }

    @Test
    void accederListaAjena_UsuarioNormal_Retorna403() {
        HttpEntity<Void> peticionGet = new HttpEntity<>(crearHeaders(tokenUsuario2));
        ResponseEntity<String> respuesta = restTemplate.exchange("/api/listas/" + listaUsuario1Id, HttpMethod.GET, peticionGet, String.class);
        
        assertEquals(HttpStatus.FORBIDDEN, respuesta.getStatusCode());
    }

    @Test
    void accederListaAjena_Admin_Retorna200() {
        HttpEntity<Void> peticionGet = new HttpEntity<>(crearHeaders(tokenAdmin));
        ResponseEntity<String> respuesta = restTemplate.exchange("/api/listas/" + listaUsuario1Id, HttpMethod.GET, peticionGet, String.class);
        
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }
}