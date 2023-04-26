package org.example.models;

import java.io.Serializable;

public class Admin implements Serializable {

    //Atributos
    private int id;
    private String nombre;
    private String clave;
    private String email;

    //Constructor
    public Admin(int id, String nombre, String clave, String email) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
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


    //Método que introduce el nombre y la contraseña, si es correcta devuelve true, si no devuelve false
    public boolean logging(String nombre, String clave){
        return nombre.equals(this.nombre) && clave.equals(this.clave);
    }


    @Override
    public String toString() {
        return  "Administrador id: " + id + '\n' +
                "Usuario: " + nombre + '\n' +
                "Clave: " + clave + '\n' +
                "Email: " + email+ '\n' +
                "=========================" +'\n';
    }
}
