package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.FavoritoRequestDto;
import ar.edu.unvime.api_blank.dto.FavoritoResponseDto;
import ar.edu.unvime.api_blank.model.Favorito;
import ar.edu.unvime.api_blank.repository.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritoService{
    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository){
        this.favoritoRepository = favoritoRepository;
    }

    public FavoritoResponseDto crear(FavoritoRequestDto requestDto){
        Favorito favorito = requestDto.aEntidad();
        Favorito guardado = favoritoRepository.guardar(favorito);
        return FavoritoResponseDto.desdeEntidad(guardado);
    }
    
    public List<FavoritoResponseDto> obtenerTodos(){
        return favoritoRepository.buscarTodos().stream()
        .map(FavoritoResponseDto::desdeEntidad)
        .toList();
    }

    public Optional<FavoritoResponseDto> obtenerPorId(Long id){
        return favoritoRepository.buscarPorId(id)
            .map(FavoritoResponseDto::desdeEntidad);
    }

    public Optional<FavoritoResponseDto> actualizar(Long id, FavoritoRequestDto requestDto){
        Optional<Favorito> existente = favoritoRepository.buscarPorId(id);

        if(existente.isEmpty()){
            return Optional.empty();
        }
        Favorito favorito = existente.get();
        favorito.setProductoId(requestDto.getProductoId());
        favorito.setNotaPersonal(requestDto.getNotaPersonal());

        Favorito actualizado = favoritoRepository.guardar(favorito);
        return Optional.of(FavoritoResponseDto.desdeEntidad(actualizado));
    }

    public boolean eliminar(Long id){
        if(!favoritoRepository.existePorId(id)){
            return false;
        } else {
            favoritoRepository.eliminarPorId(id);
            return true;
        }
    }
}