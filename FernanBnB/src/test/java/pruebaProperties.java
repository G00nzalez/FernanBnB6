import org.example.saves.Saves;
import org.example.utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

public class pruebaProperties {

    public static void main(String[] args) {
        Properties p = new Properties();
        try {
            p.load(new FileReader("config.properties"));
        } catch (IOException e) {
            System.out.println("ERROR");
        }

        p.setProperty("language","esp");
        Saves.escribeProperties("config.properties","lastAcces", Utils.formateaFecha(LocalDate.now()));

        System.out.println(p.getProperty("language"));
    }
}
