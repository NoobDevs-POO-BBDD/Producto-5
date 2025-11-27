package com.tiendaonline.model;

import jakarta.persistence.*; // Importamos JAKARTA

@Entity // 1. Marca la clase como una entidad
@Table(name = "articulos") // 2. Mapea la clase a la tabla "articulos"
public class Articulo {


    @Id // 3. Marca 'codigo' como la Clave Primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private int id;

    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;

    // 4. Mapea el campo 'precioVenta' a la columna 'precio_venta'
    @Column(name = "precio_venta")
    private double precioVenta;

    @Column(name = "gastos_envio")
    private double gastosEnvio;

    @Column(name = "tiempo_preparacion")
    private int tiempoPreparacion;

    // --- IMPORTANTE ---
    // 5. JPA requiere un constructor vacío (sin argumentos)
    public Articulo() {
    }
    // ------------------

    // Constructor original
    public Articulo(String codigo, String descripcion, double precioVenta, double gastosEnvio, int tiempoPreparacion){
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    //getters y setters (sin cambios)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(double gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    //toString (sin cambios)

    @Override
    public String toString() {
        return "Articulo{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precioVenta=" + precioVenta +
                ", gastosEnvio=" + gastosEnvio +
                ", tiempoPreparacion=" + tiempoPreparacion +
                '}';
    }
}