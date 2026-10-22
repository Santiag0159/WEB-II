package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.model.Usuario;
import ar.edu.unvime.api_blank.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // Aca volvemos a poner UsuarioService
    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrar_NuncaGuardaContraseniaEnPlano() {
        Usuario usuario = new Usuario();
        
        usuario.setEmail("test@test.com"); 
       
        usuario.setPasswordHash("claveSecreta"); 

        when(usuarioRepository.existePorEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("claveSecreta")).thenReturn("hashSeguro");
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.registrar(usuario);

        assertNotEquals("claveSecreta", resultado.getPasswordHash());
        assertEquals("hashSeguro", resultado.getPasswordHash());
        verify(passwordEncoder).encode("claveSecreta");
    }
}