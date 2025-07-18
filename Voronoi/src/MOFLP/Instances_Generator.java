package MOFLP;

import org.ajwerner.voronoi.Point;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Instances_Generator {
    public static void main(String[] args) throws IOException {
        ArrayList<Point> sites = new ArrayList<>();
        Locale.setDefault(Locale.ENGLISH);
        Scanner input = new Scanner(System.in);
        System.out.println("Introduce el archivo a leer: (- para el paper)");
        String file_name = input.next();
        if(file_name.equals("-")) file_name = "/home/sergio/TFG-Informatica_Github/Voronoi/Instancias/x=4421 y=189/listado";
        File file = new File(file_name);
        Scanner read = new Scanner(file);


        System.out.println("Introduce el numero de nodos: ");

        //Sites
        int n = 1000;
        for (int i = 0; i < n; i++) {
            double x = read.nextDouble();
            double y = read.nextDouble();
            sites.add(new Point(x, y));
        }



        for (int nodos = 100; nodos <= 1000; nodos+=100) {
            for (int i = 2; i <= 20 ; i++) {
                for (int j = 1; j <= 2; j++) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("/home/sergio/TFG-Informatica_Github/Voronoi/Instancias/x=4421 y=189/"+nodos+"_"+i+"_"+j+".txt"));
                    bw.write(nodos+" "+i+" "+j+"\n");
                    DecimalFormat df = new DecimalFormat("0.00000");
                    for (int k = 0; k < nodos; k++) {
                        bw.write(df.format(sites.get(k).x)+" "+df.format(sites.get(k).y)+"\n");
                    }
                    bw.flush();
                }
            }
        }

    }
}
