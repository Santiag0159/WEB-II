package ar.edu.unvime.api_blank.controller;

import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import ar.edu.unvime.api_blank.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api/auth")
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
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request){
        if(usuarioRepository.existePorEmail(request.email())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("el email ya esta registrado");
        }
        String hash = passwordEncoder.encode(request.password());
        Usuario nuevoUsuario = new Usuario(null, request.email(), hash, "ROLE_USER");
        usuarioRepository.guardar(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");
    }

    /**
     * @param request
     * @return
     */
    @PostMapping("/login")
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