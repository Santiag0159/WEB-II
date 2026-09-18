package ar.edu.unvime.api_blank.dto;

import ar.edu.unvime.api_blank.model.Favorito;

import java.time.LocalDateTime;

public class FavoritoResponseDto{
    private Long id;
    private Long productoId;
    private String notaPersonal;
    private LocalDateTime fechaAgregado;

    public FavoritoResponseDto() {}
    
    public FavoritoResponseDto(Long id, Long productoId, String notaPersonal, LocalDateTime fechaAgregado){
        this.id = id;
        this.productoId = productoId;
        this.notaPersonal = notaPersonal;
        this.fechaAgregado = fechaAgregado;
    }
    //Mapeo manual
    public static FavoritoResponseDto desdeEntidad(Favorito favorito){
        return new FavoritoResponseDto(
            favorito.getId(),
            favorito.getProductoId(),
            favorito.getNotaPersonal(),
            favorito.getFechaAgregado()
        );
    }

    public Long getId(){return id;}
    public Long getProductoId(){return productoId;}
    public String getNotaPersonal(){return notaPersonal;}
    public LocalDateTime getFechaAgregado(){return fechaAgregado;}

    public void setId(Long id){this.id = id;}
    public void setProductoId(Long productoId){this.productoId = productoId;}
    public void setNotaPersonal(String notaPersonal){this.notaPersonal = notaPersonal;}
    public void setFechaAgregado(LocalDateTime fechaAgregado){this.fechaAgregado = fechaAgregado;}
}