package MOFLP;

import org.ajwerner.voronoi.Point;
import org.ajwerner.voronoi.Voronoi;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main_VNS {



    public static void main(String[] args) throws IOException {
        System.out.println("version nueva 4");
        ArrayList<Point> sites = new ArrayList<>();
        Locale.setDefault(Locale.ENGLISH);
        Scanner input = new Scanner(System.in);
        System.out.println("Introduce el archivo a leer: (- para el paper)");
        String file_name = input.next();
        if(file_name.equals("-")) file_name = "Kalczynski-Drezner_data_1000.txt";
        File file = new File(file_name);
        Scanner read = new Scanner(file);


        System.out.println("Introduce el numero de nodos: ");

        //Sites
        int n = input.nextInt();
        for (int i = 0; i < n; i++) {
            double x = read.nextDouble();
            double y = read.nextDouble();
            sites.add(new Point(x, y));
        }

        //Variable para afinar la partición de aristas
        double beta = 0.0094;

        Voronoi v = new Voronoi(sites,beta);


        System.out.println("Introduzca el parametro de distancia D:");
        System.out.println("1 para 1/r(p) y 2 para 1/2r(p):");
        int mulD = input.nextInt();


        double alpha = 0.7209;

        StringBuilder scores = new StringBuilder();
        StringBuilder times = new StringBuilder();

        Conjunto_Objetivo C = new Conjunto_Objetivo(v.getGrafo(),sites);

        String bl = "ba";

        for (int p = 2; p < 21; p++) {
            long a = System.currentTimeMillis();
            //Distancia de prohibicion
            double D = 1/Math.sqrt(mulD*p);

            Conjuntos_Infactibles fs = new Conjuntos_Infactibles(v.getGrafo().keySet(),D);

            double max = 0;
            int iteraciones = n/10;

            int kmax= (int) Math.ceil(p/2.0);
            for (int i = 0; i < iteraciones || max==0; i++) {

                long seed = (long) (Math.random()*10000000);
                //Parámetro alpha de GRASP
                GRASP_Constructor g = new GRASP_Constructor(v.getGrafo(),p,D,sites,alpha,fs,seed,C);
                Solucion solucion = g.generateSolution();
                double value = solucion.eval_check(sites,D);
                if(value==0) continue;

                LocalSearch imp = new LocalSearch_Swap_fObj_BI(v.getGrafo(),p,D,sites,fs,seed,C);
                solucion = imp.improveSolution(solucion);

                value = solucion.eval_check(sites,D);
                VNS vns = new VNS(v.getGrafo(),sites,p,D,fs,C);
                solucion = vns.execute(solucion,1,kmax);



                value = solucion.eval_check(sites,D);
                //System.err.println(value);

                if(value>max){
                    max = Math.max(value,max);
                }
            }

            long b = System.currentTimeMillis();
            times.append((b-a)/1000.0).append("\n");

            scores.append(max).append("\n");
            System.out.println(max);
        }
        System.out.println();
        System.out.println(times);

    }


}
