package org.example.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    //Atributos
    private int id;
    private String nombre;
    private String clave;
    private String email;
    private ArrayList<Reserva> reservas;

    //Constructor
    public User(int id, String nombre, String clave, String email) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        this.reservas = new ArrayList<>();
    }

    //Gets

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    @Override
    public String toString() {
        return "***** USUARIO " + nombre + " *****"+ '\n' +
                "Contraseña: " + clave + '\n' +
                "Email: " + email + '\n' +
                "===== RESERVAS =====" + '\n' + ((reservas.size() == 0 ) ? "No ha realizado ninguna reserva" : reservas) +'\n';
    }

    public void addReserva(Reserva r) {
        reservas.add(r);
    }

    public boolean deleteReserva(Reserva reserva) {
        return reservas.remove(reserva);
    }
}
