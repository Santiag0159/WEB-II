package ar.edu.unvime.api_blank.dto;

import ar.edu.unvime.api_blank.model.Favorito;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; 

import java.time.LocalDateTime;

public class FavoritoRequestDto{
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotBlank(message = "La nota personal no puede estar vacia")
    @Size(max = 100, message = "La nota no puede superar los 100")
    private String notaPersonal;

    public FavoritoRequestDto() {}

    public FavoritoRequestDto(Long productoId, String notaPersonal) {
        this.productoId = productoId;
        this.notaPersonal = notaPersonal;
    }

    //Mapeo manual
    public Favorito aEntidad(){
        Favorito favorito = new Favorito();
        favorito.setProductoId(this.productoId);
        favorito.setNotaPersonal(this.notaPersonal);
        favorito.setFechaAgregado(LocalDateTime.now());
        return favorito;
    }

        public Long getProductoId(){return productoId;}
        public void setProductoId(Long productoId){this.productoId = productoId;}

        public String getNotaPersonal(){return notaPersonal;}
        public void setNotaPersonal(String notaPersonal){this.notaPersonal = notaPersonal;}
}