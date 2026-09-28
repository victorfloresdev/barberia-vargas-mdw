package com.utp.salonbarberiavargas.domain.servicios;

public class Servicio {
    private int id_servicio;
    private String nombre_servicio;
    private String descripcion;
    private int duracion_estimada;
    private double precio;

    public Servicio() {
    }

    public Servicio(int id_servicio, String nombre_servicio, String descripcion, int duracion_estimada, double precio) {
        this.id_servicio = id_servicio;
        this.nombre_servicio = nombre_servicio;
        this.descripcion = descripcion;
        this.duracion_estimada = duracion_estimada;
        this.precio = precio;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombre_servicio() {
        return nombre_servicio;
    }

    public void setNombre_servicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion_estimada() {
        return duracion_estimada;
    }

    public void setDuracion_estimada(int duracion_estimada) {
        this.duracion_estimada = duracion_estimada;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}