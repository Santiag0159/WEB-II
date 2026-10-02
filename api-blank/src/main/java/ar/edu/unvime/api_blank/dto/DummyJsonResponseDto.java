package ar.edu.unvime.api_blank.dto;
import java.util.List;

public class DummyJsonResponseDto {
    private List<DummyProductDto> products;

    public List<DummyProductDto> getProducts(){return products;}
    public void setProducts(List<DummyProductDto> products){this.products = products;}
    
}
