package ar.edu.unvime.api_blank.dto;


public class ProductoDto{
    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;

    public ProductoDto(){}

    public ProductoDto(Long id, String nombre, String descripcion, double precio, String categoria){
    
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;

    }

    //GETTERS
    public Long getId(){return id;}
    public String getNombre(){return nombre;}
    public String getDescripcion(){return descripcion;}
    public double getPrecio(){return precio;}
    public String getCategoria(){return categoria;}

    //SETTERS
    public void setId(Long id){this.id = id;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setDescripcion(String descripcion){this.descripcion = descripcion;}
    public void setPrecio(double precio){this.precio = precio;}
    public void setCategoria(String categoria){this.categoria = categoria;}

}