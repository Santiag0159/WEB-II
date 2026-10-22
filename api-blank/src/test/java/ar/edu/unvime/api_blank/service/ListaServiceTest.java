package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.model.Lista;
import ar.edu.unvime.api_blank.repository.ListaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListaServiceTest {

    @Mock
    private ListaRepository listaRepository;

    @InjectMocks
    private ListaService listaService;

    @Test
    void crear_AsociaListaAlUsuarioAutenticado() {
        Lista lista = new Lista();
        lista.setNombre("Mi Lista");
        Long usuarioAutenticadoId = 5L;

        when(listaRepository.guardar(any(Lista.class))).thenAnswer(i -> i.getArgument(0));

        Lista resultado = listaService.crear(lista, usuarioAutenticadoId);

        assertEquals(usuarioAutenticadoId, resultado.getUsuarioId());
    }
}