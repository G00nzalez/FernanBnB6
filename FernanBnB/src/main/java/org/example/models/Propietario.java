package org.example.models;

import org.example.controller.Controller;

import java.io.Serializable;
import java.util.ArrayList;

public class Propietario implements Serializable {

    Controller c = new Controller();

    //Atributos
    private int id;
    private String nombre;
    private String clave;
    private String email;
    private ArrayList<Vivienda> viviendas;

    //Constructor
    public Propietario(int id, String nombre, String clave, String email) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        this.viviendas = new ArrayList<>();
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

    public ArrayList<Vivienda> getViviendas() {
        return viviendas;
    }

    //Métodos

    @Override
    public String toString() {
        return "***** PROPIETARIO " + nombre + " *****"+ '\n' +
                "Contraseña: " + clave + '\n' +
                "Email: " + email + '\n' +
                "===== VIVIENDAS =====" + '\n' + ((viviendas.size() == 0 ) ? "No ha registrado ninguna vivienda" : viviendas) +'\n';
    }

    public boolean addVivienda(Vivienda temp) {

        for (Vivienda v:
             viviendas) {
            if (v.getId() == temp.getId()){
                viviendas.remove(v);
                viviendas.add(temp);
                return true;
            }
        }

        return viviendas.add(temp);
    }
}
