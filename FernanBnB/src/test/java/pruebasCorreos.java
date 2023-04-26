import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.example.models.Vivienda;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;


public class pruebasCorreos {
    private static Properties properties = new Properties();
    private static Session mSession;
    private static MimeMessage mCorreo;

    public static void main(String[] args) {
        Vivienda temp = new Vivienda(10,"piso","piso","Martos", "Jaen",15,50);

        String emailFrom = properties.getProperty("email");

        enviarCorreoPDF(emailFrom,temp);

    }


    //Método que junta otros 2 métodos para poder enviar un correo. Precisa de un asunto y un contenido.
    public static void enviarCorreo() {

        try {
            properties.load(new FileReader("configuracion/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String emailTo = "regalbarca1@gmail.com";
        String emailFrom = properties.getProperty("email");
        String passwordFrom = properties.getProperty("passw");

        ArrayList<Vivienda> viviendas = new ArrayList<>();
        Vivienda temp = new Vivienda(10,"piso","piso","Martos", "Jaen",15,50);

//        enviarCorreoCVS(emailFrom, viviendas);
//        sendCorreo(emailFrom, passwordFrom);

        enviarCorreoPDF(emailFrom,temp);
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


    public static void enviarCorreoCVS(String asunto, ArrayList<Vivienda> viviendas) {
        ArrayList<String> contenido = new ArrayList<>();
        contenido.add("IdVivienda;Titulo;Localidad;Provincia;MaxOcupantes;PrecioNoche");

        //Añadimos la información que queremos de cada vivienda.
        for (Vivienda v : viviendas
        ) {
            contenido.add(v.getId() + ";" + v.getTitulo() + ";" + v.getLocalidad() + ";" + v.getProvincia() + ";" + v.getMaxOcupantes() + ";" + v.getPrecioNoche());
        }

        //Generamos el archivo que vamos a enviar.
        File archivo = new File("resumen.csv");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));
            for (int i = 0; i < contenido.size(); i++) {
                bw.write(contenido.get(i) + "\n");
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


        createCorreoCSV(emailFrom, emailTo, asunto, archivo);
        sendCorreo(emailFrom, passwordFrom);

        //Eliminamos el archivo.
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

    public static void enviarCorreoPDF(String asunto,Vivienda vivienda) {


        //Generamos el archivo que vamos a enviar.
        File archivo = new File(vivienda.getTitulo()+".pdf");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write("<h1> RESUMEN DE VIVIENDA </h1>");

            bw.write("*** Vivienda "+vivienda.getId()+" ***" +'\n');
            bw.write("--------------------------"+'\n');
            bw.write("Título: "+vivienda.getTitulo()+'\n');
            bw.write("Descripción: "+vivienda.getDescripcion()+'\n');
            bw.write("Localidad: "+vivienda.getLocalidad()+'\n');
            bw.write("Provincia: "+vivienda.getProvincia()+'\n');
            bw.write("Máximo de ocupantes: "+vivienda.getMaxOcupantes()+'\n');
            bw.write("Precio por noche: "+vivienda.getPrecioNoche()+ "€");

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


}


