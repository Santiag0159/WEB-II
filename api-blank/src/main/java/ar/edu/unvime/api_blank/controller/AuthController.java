package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import ar.edu.unvime.api_blank.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión. Estos endpoints son públicos.")
public class AuthController{
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar usuario", description = "Crea una cuenta nueva. No requiere autenticación.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request){
        if(usuarioRepository.existePorEmail(request.email())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("el email ya esta registrado");
        }
        String hash = passwordEncoder.encode(request.password());
        Usuario nuevoUsuario = new Usuario(null, request.email(), hash, "ROLE_USER");
        usuarioRepository.guardar(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida el email y la contraseña y devuelve un JWT para acceder a favoritos y listas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación correcta; devuelve el token JWT",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Email o contraseña incorrectos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return usuarioRepository.buscarPorEmail(request.email())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .<ResponseEntity<?>>map(u -> {
                    // Si coincide, generamos y devolvemos el token
                    String token = jwtService.generarToken(u.getId(), u.getEmail(), u.getRol());
                    return ResponseEntity.ok(new TokenResponse(token));
                })
                // Si falla (no existe el email o mala pass), 401 Unauthorized
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Credenciales inválidas")));
    }

    public record LoginRequest(String email, String password){}
    public record RegistroRequest(String email, String password){}
    public record TokenResponse(String token){}
    public record ErrorResponse(String mensaje){}
}