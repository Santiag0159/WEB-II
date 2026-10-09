package ar.edu.unvime.api_blank.dto;

import java.time.LocalDateTime;

public class ListaResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;

    public ListaResponseDto(Long id, String nombre, String descripcion, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}