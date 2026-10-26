package com.utp.salonbarberiavargas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @NotNull(message = "Debe seleccionar un cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    @NotNull(message = "Debe seleccionar un servicio")
    private Servicio servicio;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    @NotBlank(message = "La hora es obligatoria")
    private String hora;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    private Double precio;

    @NotBlank(message = "El estado de la cita es obligatorio")
    private String estadoCita;

    private String barbero;

    public Cita() {
    }

    public Cita(Integer idCita, Cliente cliente, Servicio servicio, String fecha, String hora, Double precio, String estadoCita, String barbero) {
        this.idCita = idCita;
        this.cliente = cliente;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.estadoCita = estadoCita;
        this.barbero = barbero;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }

    public String getBarbero() {
        return barbero;
    }

    public void setBarbero(String barbero) {
        this.barbero = barbero;
    }

    public String getNombreCliente() {
        return cliente != null ? cliente.getNombreCompleto() : "";
    }

    public String getTelefono() {
        return cliente != null ? cliente.getTelefono() : "";
    }

    public String getNombreServicio() {
        return servicio != null ? servicio.getNombre_servicio() : "";
    }
}
