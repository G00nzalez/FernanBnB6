package org.example.saves;

import org.example.controller.Controller;
import org.example.models.*;
import org.example.utils.Utils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class Saves {

    //Método para escribir en el log lo que está ocurriendo para los propietarios.
    public static void escribeLogPropietario(String parametro, Propietario propietario, int id) {
        String mensaje = generaMensajeLogPropietario(parametro, propietario, id);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt", true));
            bw.write(mensaje);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Método para escribir en el log lo que está ocurriendo para los administradores.
    public static void escribeLogAdministrador(String parametro, Admin a) {
        String mensaje = generaMensajeLogAdmin(parametro, a);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt", true));
            bw.write(mensaje);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Método para escribir en el log lo que está ocurriendo para los usuarios.
    public static void escribeLogUsuario(String parametro, User u, int id) {
        String mensaje = generaMensajeLogUser(parametro, u, id);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt", true));
            bw.write(mensaje);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //Método para generar el mensaje que se va a escribir en el log sobre lo que está ocurriendo para los usuarios.
    private static String generaMensajeLogUser(String parametro, User u, int id) {
        //Cuando el usuario haga inicio de sesión.
        if (parametro.equals("inicio sesion")) {
            return "Inicio de sesión;" + u.getNombre() + ";Usuario;" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        //Cuando el usuario cierra sesión.
        if (parametro.equals("cerrar sesion")) {
            return "Cierre de sesión;" + u.getNombre() + ";Usuario;" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        //Cuando el usuario realiza una reserva.
        if (parametro.equals("nueva reserva")) {
            return "Nueva reserva;" + u.getNombre() + ";" + id + ";" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        return "Error usuario.";
    }

    //Método para generar el mensaje que se va a escribir en el log sobre lo que está ocurriendo para los propietarios.
    private static String generaMensajeLogPropietario(String parametro, Propietario p, int id) {
        //Cuando el propietario haga inicio de sesión.
        if (parametro.equals("inicio sesion")) {
            return "Inicio de sesión;" + p.getNombre() + ";Propietario;" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        //Cuando el propietario cierre sesión.
        if (parametro.equals("cerrar sesion")) {
            return "Cierre de sesión;" + p.getNombre() + ";Propietario;" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        //Cuando el propietario establece un periodo de no disponibilidad.
        if (parametro.equals("indisponibilidad")) {
            return "Periodo de no disponibilidad;" + p.getNombre() + ";" + id + ";" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        //Cuando el propietario elimina una vivienda.
        if (parametro.equals("eliminar vivienda")) {
            return "Eliminación de vivienda;" + p.getNombre() + ";" + id + ";" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        //Cuando el propietario inserta una vivienda.
        if (parametro.equals("insertar vivienda")) {
            return "Inserción de vivienda;" + p.getNombre() + ";" + id + ";" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }


        return "Error propietario.";
    }

    //Método para generar el mensaje que se va a escribir en el log sobre lo que está ocurriendo para los administradores.
    private static String generaMensajeLogAdmin(String parametro, Admin a) {
        //Cuando el admin haga inicio de sesión.
        if (parametro.equals("inicio sesion")) {
            return "Inicio de sesión;" + a.getNombre() + ";Administrador;" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        //Cuando el admin cierre sesión.
        if (parametro.equals("cerrar sesion")) {
            return "Cierre de sesión;" + a.getNombre() + ";Administrador;" + Utils.formateaFechaHora(LocalDateTime.now()) + '\n';
        }

        return "Error administrador.";
    }


    //Método para guardar una copia de seguridad.
    public static boolean generarBackUp(Controller c, String ruta) {
        ObjectOutputStream oos;
        try {
            FileOutputStream fisal = new FileOutputStream(ruta + "/backup.out");
            oos = new ObjectOutputStream(fisal);
            oos.writeObject(c);
            oos.close();
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

    }

    //Método para recuperar una copia de seguridad.
    public static boolean existeBackUp(String ruta) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(ruta);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object c = ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    //Método para recuperar un backup previamente creado.
    public static Object recuperaBackUp(String ruta) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(ruta);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    //Método properties
    public static boolean escribeUltimoAccesoUsuario(Properties p, User u ){

        try {
            p.load(new FileReader("configuracion/config.properties"));
            p.setProperty(u.getEmail()+"LastAccess", Utils.formateaFecha(LocalDate.now()));
            return true;

        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

    }

    public static boolean escribeProperties(String ruta,String clave,String valor){

        try {
            Properties p = new Properties();
            p.load(new FileReader(ruta));
            p.setProperty(clave,valor);

            String mensaje = clave + "=" + valor + "\n";

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true));
                bw.write(mensaje);
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return true;

        } catch (IOException e) {
            System.out.println(e);
            return false;
        }

    }

}
