package MOFLP;

import org.ajwerner.voronoi.Point;
import org.ajwerner.voronoi.Voronoi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Prueba500 {

    /*

    public static void main(String[] args) throws IOException {

        File[] logs = new File[76];
        for (int i = 0; i < 76; i++) {
            String direc = "/home/sergio/TFG-Informatica_Github/Voronoi/logs_prueba500";
            String nombre = "";
            if(i<38) nombre += "n=100 ";
            else nombre += "n=1000 ";
            if((0<=i && i< 19) || (38<=i && i<57)) nombre+="D=rp ";
            else nombre+="D=r2p ";
            int aux = i%19;
            aux+=2;
            nombre+="p="+aux;


            logs[i]=new File(direc,nombre);
            logs[i].createNewFile();

        }



        ArrayList<Point> sites = new ArrayList<>();
        Locale.setDefault(Locale.ENGLISH);
        File file = new File("/home/sergio/TFG-Informatica_Github/Voronoi/Kalczynski-Drezner_data_1000.txt");
        Scanner in = new Scanner(file);
        Voronoi v=null;
        for (int it = 0; it < 76; it++) {
            if(it==0){
                //Sites
                int n = 100;
                for (int i = 0; i < n; i++) {
                    double x = in.nextDouble();
                    double y = in.nextDouble();
                    sites.add(new Point(x, y));
                }
                v = new Voronoi(sites,0.02);
            }

            if(it==38){
                for (int i = 0; i < 900; i++) {
                    double x = in.nextDouble();
                    double y = in.nextDouble();
                    sites.add(new Point(x, y));
                }
                v = new Voronoi(sites,0.01);
            }

            FileWriter fw = new FileWriter(logs[it]);

            int p = it%19;
            p+=2;
            //Distancia de prohibicion

            double D;
            if((0<=it && it< 19) || (38<=it && it<57)) D=1/Math.sqrt(p);
            else D=1/Math.sqrt(2*p);
            double max = 0;
            int iteraciones = 500;
            boolean flag = true;
            for (int i = 0; i < iteraciones && flag; i++) {
                long seed = (long) (Math.random()*10000000);
                //Parámetro alpha de GRASP
                double alpha = 0.5;
                GRASP_Constructor g = new GRASP_Constructor(v.getGrafo(),p,D,sites,alpha,seed);
                Solution solucion = g.generateSolution();
                LocalSearch_Swap_fObj_FI imp = new LocalSearch_Swap_fObj_FI(v.getGrafo(),p,D,sites,seed);
                solucion = imp.improveSolution(solucion);

                Evaluator ev = new Evaluator( sites, D);

                double value = solucion.eval_check(sites,D);
                fw.write(value+"\n");

                max = Math.max(value,max);

            }
            fw.flush();
            fw.close();
            System.out.println(max);


        }




    }

     */


}
