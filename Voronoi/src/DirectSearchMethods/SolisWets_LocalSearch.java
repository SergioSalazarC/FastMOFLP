
package DirectSearchMethods;

import MOFLP.Conjuntos_Infactibles;
import MOFLP.LocalSearch;
import MOFLP.Solucion;
import org.ajwerner.voronoi.Point;

import java.util.*;

public class SolisWets_LocalSearch implements LocalSearch {

    static boolean isFactible(Point p, Solucion sol, double D){
        if(p.x<0 || p.x>1)return false;
        if(p.y<0 || p.y>1) return false;
        for(Point q : sol){
            if(p.distanceTo(q)<D) return false;
        }
        return true;
    }

    static double pointEvaluator(Point p, Solucion sol, double D, ArrayList<Point> sites){
        if(!isFactible(p,sol,D))return 0;

        double min = Double.MAX_VALUE;
        for (Point site :sites){
            min = Math.min(min, p.distanceTo(site));
        }
        return -min;
    }

    private static class evalFunction extends Function{

        public evalFunction(String name, int dimension, double lowerBound, double upperBound) {
            super(name, dimension, lowerBound, upperBound);
        }

        @Override
        protected double internalEvaluate(double[] sol) {
            Point aux = new Point(sol[0],sol[1]);
            return pointEvaluator(aux,solution,D,sites);
        }
    }

    private static ArrayList<Point> sites;
    //Cantidad de facilities a colocar
    private int p;
    //Distancia entre facilities
    private static double D;

    //Variable para la solución.
    private static Solucion solution;

    private Random random;

    private Conjuntos_Infactibles fs;

    private Comparator<Point> comp_descending = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            double o1v = pEv(o1);
            double o2v = pEv(o2);
            //Ordenamos de mayor a menor
            return Double.compare(o2v,o1v);
        }
    };




    public SolisWets_LocalSearch(HashMap<Point, LinkedList<Point>> grafo, int p, double d, ArrayList<Point> sites, Conjuntos_Infactibles fs , long seed) {

        this.sites = sites;
        this.p = p;
        D = d;
        random=new Random(seed);
        this.fs = fs;

    }


    public Solucion improveSolution(Solucion sol) {
        this.solution=sol;
        solution.sort(comp_descending);

        Point elim = solution.removeFacility(solution.size()-1);

        evalFunction f = new evalFunction("evaluacion",2,0,Math.sqrt(2));

        DirectSearchImprovement ds = new SolisWets();

        double[] aux = new double[]{elim.x, elim.y};

        ds.improveSolution(aux, f ,0.0001,1000);

        Point add = new Point(aux[0],aux[1]);

        solution.addFacility(add);
        return solution;



    }






    private double pEv(Point p){
        double min = Double.MAX_VALUE;
        for (Point site :sites){
            min = Math.min(min, p.distanceTo(site));
        }
        return min;
    }
}


