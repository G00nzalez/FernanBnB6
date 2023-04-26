package org.example.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Vivienda implements Comparable<Vivienda>, Serializable {

    //Atributos
    private int id;
    private String titulo;
    private String descripcion;
    private String localidad;
    private String provincia;
    private int maxOcupantes;
    private double precioNoche;
    private ArrayList<Reserva> reservas;


    //Constructor
    public Vivienda(int id, String titulo, String descripcion, String localidad, String provincia, int maxOcupantes, double precioNoche) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.maxOcupantes = maxOcupantes;
        this.precioNoche = precioNoche;
        this.reservas = new ArrayList<>();
    }

    //Gets
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public int getMaxOcupantes() {
        return maxOcupantes;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    //Métodos

    @Override
    public String toString() {
        return "*** Vivienda " + id + " ***" + '\n' +
                "Titulo: " + titulo + '\n' +
                "Descripción: " + descripcion + '\n' +
                "Localidad: " + localidad + '\n' +
                "Provincia: " + provincia + '\n' +
                "Máximo de ocupantes: " + maxOcupantes + '\n' +
                "Precio por noche: " + precioNoche + '\n' +
                "----- Reservas -----" + '\n' + ((reservas.isEmpty()) ? "No ha registrado ninguna vivienda" : reservas);
    }

    @Override
    public int compareTo(Vivienda o) {
        return localidad.compareTo(o.localidad);
    }

    //Método que devuelve true si se puede realizar la reserva por fecha y devuelve false si coincide algún dia.
    public boolean tieneDisponibilidad(LocalDate fecha, int noches) {
        if (reservas.isEmpty()) return true;
        for (Reserva r:
             reservas) {
            for (int i = 0; i < noches; i++) {
                if (r.getFechaInicio() == fecha.plusDays(i)) return false;
            }
        }

        return true;

    }
}
