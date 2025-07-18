package MOFLP;

import org.ajwerner.voronoi.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TestGenerator {
    public static void main(String[] args) throws IOException {
        String archivo = "listado";

        generateCase(archivo,97,367,1000);
    }

    public static void generateCase(String file) throws IOException {
        generateCase(file,97,367,1000);
    }

    public static void generateCase(String file, long seedX, long seedY, int size) throws IOException {
        FileWriter myWriter;
        if(file==null){
            myWriter = new FileWriter("testCase.txt");
        }
        else{
            myWriter = new FileWriter(file);
        }
        long rX = seedX;
        long rY = seedY;
        ArrayList<Point> caso = new ArrayList<>(size+1);
        caso.add(new Point(rX/100000.0, rY/100000.0));
        for (int i = 1; i < size; i++) {
            long thetax = 12219*rX;
            long thetay = 12219*rY;

            rX = thetax%100000;
            rY = thetay%100000;
            caso.add(new Point(rX/100000.0, rY/100000.0));
        }
        Locale.setDefault(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00000");
        for (Point k : caso){
            myWriter.write(df.format(k.x)+" "+df.format(k.y)+"\n");
        }
        myWriter.flush();
        myWriter.close();





    }

}
