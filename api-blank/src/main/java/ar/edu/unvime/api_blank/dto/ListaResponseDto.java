package ar.edu.unvime.api_blank.dto;

import java.time.LocalDateTime;

public class ListaResponseDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;

    // 1. OBLIGATORIO: Constructor vacio para que el test pueda leer el JSON
    public ListaResponseDto() {
    }

    // 2. Constructor con parametros para el controlador
    public ListaResponseDto(Long id, String nombre, String descripcion, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}