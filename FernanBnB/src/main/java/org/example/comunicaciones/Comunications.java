package org.example.comunicaciones;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.example.models.Reserva;
import org.example.models.Vivienda;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Comunications {
    private static Properties properties = new Properties();
    private static Session mSession;
    private static MimeMessage mCorreo;


    //Método que junta otros 2 métodos para poder enviar un correo. Precisa de un asunto y un contenido.
    public static void enviarCorreo(String asunto, String contenido) {

        try {
            properties.load(new FileReader("configuracion/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String emailTo = "regalbarca1@gmail.com";
        String emailFrom = properties.getProperty("email");
        String passwordFrom = properties.getProperty("passw");

        createCorreo(emailFrom,emailTo,asunto, contenido);
        sendCorreo(emailFrom,passwordFrom);


    }

    private static void createCorreo(String emailFrom, String emailTo, String asunto, String contenido) {

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", emailFrom);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.ssl.auth", "true");

        mSession = Session.getDefaultInstance(properties);

        try {
            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mCorreo.setSubject(asunto);
            mCorreo.setText(contenido, "ISO-8859-1", "html");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private static void sendCorreo(String emailFrom, String passwordFrom) {

        try {
            Transport mTransport = mSession.getTransport("smtp");
            mTransport.connect(emailFrom, passwordFrom);
            mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransport.close();

            System.out.println("Correo enviado.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    private static void createCorreoPDF(String emailFrom, String emailTo, String asunto, File archivo) {

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", emailFrom);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.ssl.auth", "true");

        mSession = Session.getDefaultInstance(properties);

        try {
            BodyPart texto = new MimeBodyPart();
            texto.setText("Resumen de viviendas");

            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo)));
            adjunto.setFileName("resumen.pdf");

            MimeMultipart m = new MimeMultipart();
            m.addBodyPart(texto);
            m.addBodyPart(adjunto);

            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mCorreo.setSubject(asunto);
            mCorreo.setContent(m);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }


    public static void enviarCorreoCVS(String asunto, ArrayList<Vivienda> viviendas) {
        ArrayList<String> contenido = new ArrayList<>();
        contenido.add("IdVivienda;Titulo;Localidad;Provincia;MaxOcupantes;PrecioNoche");

        //Añadimos la información que queremos de cada vivienda.
        for (Vivienda v: viviendas
             ) {
            contenido.add(v.getId()+";"+v.getTitulo()+";"+v.getLocalidad()+";"+v.getProvincia()+";"+v.getMaxOcupantes()+";"+v.getPrecioNoche());
        }

        //Generamos el archivo que vamos a enviar.
        File archivo = new File("resumen.csv");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo,true));
            for (int i = 0; i < contenido.size(); i++) {
                bw.write(contenido.get(i)+"\n");
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            properties.load(new FileReader("configuracion/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String emailTo = "regalbarca1@gmail.com";
        String emailFrom = properties.getProperty("email");
        String passwordFrom = properties.getProperty("passw");


        createCorreoCSV(emailFrom,emailTo,asunto,archivo);
        sendCorreo(emailFrom,passwordFrom);

        archivo.delete();


    }

    private static void createCorreoCSV(String emailFrom, String emailTo, String asunto, File archivo) {

            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.port", "587");
            properties.setProperty("mail.smtp.user", emailFrom);
            properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.setProperty("mail.smtp.ssl.auth", "true");

            mSession = Session.getDefaultInstance(properties);

        try {
            BodyPart texto = new MimeBodyPart();
            texto.setText("Resumen de viviendas");

            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo)));
            adjunto.setFileName("resumen.csv");

            MimeMultipart m = new MimeMultipart();
            m.addBodyPart(texto);
            m.addBodyPart(adjunto);

            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mCorreo.setSubject(asunto);
            mCorreo.setContent(m);



        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }



    }

    public static void enviarCorreoPDF(String asunto, Reserva reserva) {


        //Generamos el archivo que vamos a enviar.
        File archivo = new File(reserva.getId()+".pdf");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write("<h1> RESUMEN DE VIVIENDA </h1>");

            bw.write("*** RESERVA "+reserva.getId()+" ***" +'\n');
            bw.write("--------------------------"+'\n');
            bw.write("Vivienda: "+reserva.getIdVivienda()+'\n');
            bw.write("Fecha de inicio: "+reserva.getFechaInicio()+'\n');
            bw.write("Noches: "+reserva.getNoches()+'\n');
            bw.write("Ocupantes: "+reserva.getOcupantes()+'\n');
            bw.write("Precio: "+reserva.getPrecio()+"€"+'\n');

            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            properties.load(new FileReader("configuracion/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String emailTo = "regalbarca1@gmail.com";
        String emailFrom = properties.getProperty("email");
        String passwordFrom = properties.getProperty("passw");


        createCorreoPDF(emailFrom, emailTo, asunto, archivo);
        sendCorreo(emailFrom, passwordFrom);

        //Eliminamos el archivo.
        archivo.delete();

    }
}
