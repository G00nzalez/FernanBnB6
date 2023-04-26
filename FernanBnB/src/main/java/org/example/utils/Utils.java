package org.example.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Utils {

    public static Scanner s = new Scanner(System.in);

    //Método para hacer la animación de cerrado
    public static void cerrarPrograma() {
        for (int i = 0; i < 4; i++) {
            System.out.print(".");
            try {
                Thread.sleep(800);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }

        }
        System.out.println();
        System.out.println();
        System.out.println();
    }

    //Método para parar y pulsar para continuar
    public static void pulseParaContinuar(){
        System.out.println("Pulse para continuar");
        s.nextLine();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    //Método para formatear una fecha con su hora
    public static String formateaFechaHora(LocalDateTime fechaHora){
        // Crea un objeto DateTimeFormatter con el patrón de formato deseado
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Formatea la fecha y hora actual según el patrón de formato
        String fechaHoraFormateada = fechaHora.format(formato);

        return fechaHoraFormateada;

    }

    //Método para formatear una fecha sin hora.
    public static String formateaFecha(LocalDate fecha){
        // Crea un objeto DateTimeFormatter con el patrón de formato deseado
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Formatea la fecha y hora actual según el patrón de formato
        String fechaFormateada = fecha.format(formato);

        return fechaFormateada;

    }


}
