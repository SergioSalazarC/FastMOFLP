package MOFLP;

import org.ajwerner.voronoi.Point;
import org.ajwerner.voronoi.Voronoi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Executable_Irace {
    private static double pointEvaluator(Point p, ArrayList<Point> sites){
        double min = Double.MAX_VALUE;
        for (Point site :sites){
            min = Math.min(min, p.distanceTo(site));
        }
        return min;
    }

    public static void main(String[] args) throws IOException {
        /*
        FORMATO DE LA INSTANCIA:
        nodos p D
        x_0 y_0
        x_1 y_1
        ...

        PARAMETROS
        Instancia beta alpha_grasp orden
         */
        //System.out.println("version IRACE");

        Locale.setDefault(Locale.ENGLISH);
        Scanner input = new Scanner(System.in);
        String file_name = args[0];
        if(file_name.equals("-")) file_name = "Kalczynski-Drezner_data_1000.txt";
        File file = new File(file_name);
        Scanner read = new Scanner(file);

        //Sites
        int n = read.nextInt();
        int p = read.nextInt();
        int mulD = read.nextInt();
        ArrayList<Point> sites = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double x = read.nextDouble();
            double y = read.nextDouble();
            sites.add(new Point(x, y));
        }

        //Variable para afinar la partición de aristas
        double beta = n/10000.0;

        Voronoi v = new Voronoi(sites,beta);



        int it = n/10;

        Conjunto_Objetivo C = new Conjunto_Objetivo(v.getGrafo(),sites);

        //Distancia de prohibicion
        double D = 1/Math.sqrt(mulD*p);

        Conjuntos_Infactibles fs = new Conjuntos_Infactibles(v.getGrafo().keySet(),D);

        double max = 0;
        int kmax= (int) Math.ceil(p/2.0);
        for (int i = 0; i < it || max==0; i++) {

            long seed = (long) (Math.random()*10000000);
            //Parámetro alpha de GRASP
            double alpha = Double.parseDouble(args[1]);
            GRASP_Constructor g = new GRASP_Constructor(v.getGrafo(),p,D,sites,alpha,fs,seed,C);
            Solucion solucion = g.generateSolution();
            double value = solucion.eval_check(sites,D);
            if(value==0) continue;

            LocalSearch imp = new LocalSearch_Swap_fObj_BI(v.getGrafo(),p,D,sites,fs,seed,C);
            solucion = imp.improveSolution(solucion);

            GVNS gvns = new GVNS(v.getGrafo(),sites,p,D,fs,C);
            if(args[2].equals("ab")) solucion = gvns.execute(solucion,1,3,kmax);
            else solucion = gvns.execute(solucion,3,1,kmax);

            value = solucion.eval_check(sites,D);

            if(value>max){
                max = Math.max(value,max);
            }
        }
        System.out.println(max);

    }
}
