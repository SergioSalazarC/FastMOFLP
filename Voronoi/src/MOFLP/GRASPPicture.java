package MOFLP;

import org.ajwerner.voronoi.Point;
import org.ajwerner.voronoi.Voronoi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GRASPPicture {
    /*

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Point> sites = new ArrayList<>();
        Locale.setDefault(Locale.ENGLISH);
        File file = new File("/home/sergio/TFG-Informatica_Github/Voronoi/Kalczynski-Drezner_data_1000.txt");

        Scanner in = new Scanner(file);

        for (int i = 0; i < 100; i++) {
            double x = in.nextDouble();
            double y = in.nextDouble();
            sites.add(new Point(x, y));
        }

        StdDraw.setCanvasSize(600, 600);
        StdDraw.setScale(0, 1);

        Voronoi v = new Voronoi(sites,0.02);
        v.show();




        for (Point k : v.getGrafo().keySet()){
            k.draw(StdDraw.DARK_GRAY);
        }

        double max = 0;
        int p=15;
        Solution bestsol = new Solution(p);

        for (int i = 0; i < 2000; i++) {

            double D = 1 / Math.sqrt(p);
            HashMap<Point, LinkedList<Point>> grafo = v.getGrafo();
            long seed = (long) (Math.random()*10000000);
            GRASP_Constructor constructor = new GRASP_Constructor(grafo,p,D,sites,0.5,seed);
            Solution sol = constructor.generateSolution();
            //GRASP_Improvement_fObj improver = new GRASP_Improvement_fObj(grafo,p,D,sites,seed);
            LocalSearch_Swap_f2 improver = new LocalSearch_Swap_f2(grafo,p,D,sites,seed);
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
        }
        for (Point k : bestsol){
            k.draw(StdDraw.RED);
        }


        //System.out.println(ev.getWorstSite());
        //System.out.println(ev.getWorstFacility());



    }

     */


}
