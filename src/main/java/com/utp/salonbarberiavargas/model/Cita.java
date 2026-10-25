package com.utp.salonbarberiavargas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Cita {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private String idCita;
    private String nombreCliente;
    private String telefono;
    private String nombreServicio;
    private String fecha;
    private String hora;
    private double precio;
    private String estadoCita;

    public Cita() {
    }

    public Cita(String idCita, String nombreCliente, String telefono, String nombreServicio, String fecha, String hora, double precio, String estadoCita) {
        this.idCita = idCita;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.nombreServicio = nombreServicio;
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.estadoCita = estadoCita;
    }

    public Cita(String idCita, String nombreCliente, String nombreServicio, String fecha, String hora, double precio, String estadoCita) {
        this(idCita, nombreCliente, "", nombreServicio, fecha, hora, precio, estadoCita);
    }

    public String getIdCita() {
        return idCita;
    }

    public void setIdCita(String idCita) {
        this.idCita = idCita;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }
}
