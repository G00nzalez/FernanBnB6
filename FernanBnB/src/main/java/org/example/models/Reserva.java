package org.example.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable {

    //Atributos
    private int id;
    private int idVivienda;
    private int idUsuario;
    private LocalDate fechaInicio;
    private int noches;
    private double precio;
    private int ocupantes;

    //Constructor
    public Reserva(int id, int idVivienda, int idUsuario, LocalDate fechaInicio, int noches, double precio, int ocupantes) {
        this.id = id;
        this.idVivienda = idVivienda;
        this.idUsuario = idUsuario;
        this.fechaInicio = fechaInicio;
        this.noches = noches;
        this.precio = precio;
        this.ocupantes = ocupantes;
    }

    //Gets
    public int getId() {
        return id;
    }

    public int getIdVivienda() {
        return idVivienda;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public int getNoches() {
        return noches;
    }

    public double getPrecio() {
        return precio;
    }

    public int getOcupantes() {
        return ocupantes;
    }

    //Métodos
    @Override
    public String toString() {
        return  '\n' +"*********************"  + '\n' +
                "Reserva: " + id + '\n' +
                "Id de la vivienda: " + idVivienda + '\n' +
                "Fecha de inicio: " + fechaInicio + '\n' +
                "Noches:" + noches +  '\n' +
                "Ocupantes: " + (ocupantes == -1 ? "Reserva periodo de no disponibilidad" : ocupantes)+ '\n' +
                "Precio total:" + (ocupantes == -1 ? "Reserva periodo de no disponibilidad" : precio * noches)+  '\n' +
                "*********************" + '\n';
    }
}
