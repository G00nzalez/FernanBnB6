import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class pruebas {

    public static void main(String[] args) {
        ArrayList<String> csv = new ArrayList<>();

        csv.add("IdVivienda;Titulo;Localidad;Provincia;MaxOcupantes;PrecioNoche");
        csv.add("10;casa;Torredelcampo;Jaen;10;20");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("prueba.csv",true));
            for (int i = 0; i < csv.size(); i++) {
                bw.write(csv.get(i)+"\n");
                System.out.println("Escritura");
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
