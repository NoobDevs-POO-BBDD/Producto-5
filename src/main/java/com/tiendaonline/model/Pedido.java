package com.tiendaonline.model;
import com.tiendaonline.model.Cliente;
import com.tiendaonline.model.ClientePremium;
import com.tiendaonline.model.Articulo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;
    @Column(name = "numero_pedido", nullable = false, unique = true)
    private String numeroPedido;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_articulo", nullable = false)
    private Articulo articulo;
    @Column(nullable = false)
    private int cantidad;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    @Column(nullable = false)
    private boolean estado;
    @Transient
    private double precioTotal;

    public Pedido() {}

    // Constructor
    public Pedido(String numeroPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora, boolean estado) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.precioTotal = calcularPrecioTotal(); // cálculo seguro
    }

    // Getters y setters
    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.precioTotal = calcularPrecioTotal();
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
        this.precioTotal = calcularPrecioTotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.precioTotal = calcularPrecioTotal();
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    // Metodo seguro para calcular el precio total
    private double calcularPrecioTotal() {
        if (articulo == null) {
            System.out.println("Advertencia: Pedido " + numeroPedido + " no tiene artículo asignado.");
            return 0.0;
        }

        double precioBase = articulo.getPrecioVenta() * cantidad;
        double gastosEnvio = articulo.getGastosEnvio();

        if (cliente instanceof ClientePremium premium) {
            gastosEnvio *= (1 - premium.getDescuentoEnvio() / 100.0);
        }

        return precioBase + gastosEnvio;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido='" + numeroPedido + '\'' +
                ", cliente=" + cliente +
                ", articulo=" + articulo +
                ", cantidad=" + cantidad +
                ", fechaHora=" + fechaHora +
                ", estado=" + estado +
                ", precioTotal=" + String.format("%.2f", precioTotal) +
                '}';
    }
}
