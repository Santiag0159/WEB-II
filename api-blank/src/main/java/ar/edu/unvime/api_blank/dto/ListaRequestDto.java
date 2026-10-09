package ar.edu.unvime.api_blank.dto;

import jakarta.validation.constraints.NotBlank;

public class ListaRequestDto {

    @NotBlank(message = "El nombre de la lista es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}