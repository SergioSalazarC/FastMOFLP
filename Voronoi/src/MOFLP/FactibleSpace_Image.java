package MOFLP;


import org.ajwerner.voronoi.Point;
import org.ajwerner.voronoi.Voronoi;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FactibleSpace_Image {


    public static void main(String[] args) throws FileNotFoundException {


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
        double beta = 0.8373;

        Voronoi v = new Voronoi(sites,beta);

        v.show();







        for (Point k : sites) {
            Color c = new Color(200, 0, 0, 255);
        }


        /*
        double max = 0;

        int p=14;
        Solution bestsol = new Solution(p);
        double D = 1 / Math.sqrt(2*p);


        for (int i = 0; i < 300; i++) {


            HashMap<Point, LinkedList<Point>> grafo = v.getGrafo();
            long seed = (long) (Math.random()*10000000);
            GRASP_Constructor constructor = new GRASP_Constructor(grafo,p,D,sites,0.5,seed);
            Solution sol = constructor.generateSolution();
            //GRASP_Improvement_fObj improver = new GRASP_Improvement_fObj(grafo,p,D,sites,seed);
            LocalSearch_Swap_fObj_BI improver = new LocalSearch_Swap_fObj_BI(grafo,p,D,sites,seed);
            sol = improver.improveSolution(sol);



            if (sol == null) System.out.println("(" + p + ")  " + "SIN SOLUCION");
            else {
                Evaluator ev = new Evaluator( sites, D);
                double value = sol.eval_check(sites,D);
                if(value>max){
                    bestsol=sol;
                    max=value;
                }
            }
            System.err.println(i);
        }
        System.out.println(max);
        for (Point k : bestsol){
            k.draw(StdDraw.BLUE);
            Color c = new Color(205,0,30,150);
            StdDraw.setPenColor(c);

            StdDraw.filledCircle(k.getX(),k.getY(),D);
        }



         */


    }
}
