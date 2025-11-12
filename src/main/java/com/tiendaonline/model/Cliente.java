package main.java.com.tiendaonline.model;

public abstract class Cliente {
    private String email;
    private String nombre;
    private String domicilio;
    private String NIF;

    public Cliente(String email, String nombre, String domicilio, String NIF) {
        this.email = email;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.NIF = NIF;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", NIF='" + NIF + '\'' +
                '}';
    }
}