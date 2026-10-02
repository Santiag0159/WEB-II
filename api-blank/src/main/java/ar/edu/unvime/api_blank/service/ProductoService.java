package ar.edu.unvime.api_blank.service;

import ar.edu.unvime.api_blank.dto.DummyJsonResponseDto;
import ar.edu.unvime.api_blank.dto.DummyProductDto;
import ar.edu.unvime.api_blank.dto.ProductoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductoService{
    private final RestClient restClient;

    public ProductoService(){
        this.restClient = RestClient.builder()
        .baseUrl("https://dummyjson.com")
        .build();
    }

    public List<ProductoDto> obtenerTodos(){
        DummyJsonResponseDto response = restClient.get()
        .uri("/products")
        .retrieve()
        .body(DummyJsonResponseDto.class);
    
        if(response == null || response.getProducts() == null){
            return List.of();
        }
        return response.getProducts().stream()
        .map(this::mapearADto)
        .toList();
    }
    public ProductoDto obtenerPorId(Long id) {
        DummyProductDto dummyProduct = restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(DummyProductDto.class);

        if (dummyProduct == null) {
            return null;
        }

        return mapearADto(dummyProduct);
    }
    private ProductoDto mapearADto(DummyProductDto dummy) {
        return new ProductoDto(
                dummy.getId(),
                dummy.getTitle(),
                dummy.getDescription(),
                dummy.getPrice(),
                dummy.getCategory()
        );
    }
}