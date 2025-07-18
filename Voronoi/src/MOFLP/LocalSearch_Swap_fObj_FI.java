package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;


public class LocalSearch_Swap_fObj_FI implements LocalSearch {


    //Sites
    private  ArrayList<Point> sites;
    //Cantidad de facilities a colocar
    private int p;
    //Distancia entre facilities
    private double D;

    //Variable para la solución.
    private Solucion solution;
    //Coste para cada candidato
    private HashMap<Point,Double> C;

    private Random random;

    private Conjuntos_Infactibles fs;

    private Comparator<Point> comp_descending = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            if(!C.containsKey(o1)) C.put(o1,pointEvaluator(o1));
            if(!C.containsKey(o2)) C.put(o2,pointEvaluator(o2));
            double o1v = C.get(o1);
            double o2v = C.get(o2);
            //Ordenamos de mayor a menor
            return Double.compare(o2v,o1v);
        }
    };




    public LocalSearch_Swap_fObj_FI(HashMap<Point, LinkedList<Point>> grafo, int p, double D, ArrayList<Point> sites, Conjuntos_Infactibles fs, long seed) {

        this.sites = sites;
        this.p = p;
        this.D = D;
        C=new HashMap<>();
        for (Point vp : grafo.keySet()) {//O(n²)
            double value = pointEvaluator(vp); //O(n)
            C.put(vp,value);
        }
        random=new Random(seed);
        this.fs = fs;

    }


    public Solucion improveSolution(Solucion sol) {
        this.solution=sol;
        solution.sort(comp_descending); //O(plogp) <<<<< O(n)
        double valor_solucion = C.get(solution.getFacility(solution.size()-1));
        boolean flag_cambia = true;
        while(flag_cambia && solution.size()==p){//O(n²)
            //FIRST-IMPROVEMENT
            //Función de coste para fase de mejora  =  Maximizar la minima distancia
            //Misma función que la objetivo
            Point peor = solution.removeFacility(solution.size()-1);
            flag_cambia=false;
            for(Point k : sol.getFactibles()){
                if(C.get(k)>valor_solucion){
                    solution.addFacility(k);
                    flag_cambia=true;
                    solution.sort(comp_descending);
                    valor_solucion = C.get(solution.getFacility(solution.size()-1));
                    break;
                }
            }
            if(!flag_cambia) solution.addFacility(peor);
        }

        return solution;
    }



    private double pointEvaluator(Point p){
        double min = Double.MAX_VALUE;
        for (Point site :sites){
            min = Math.min(min, p.dist2(site));
        }
        return min;
    }





}
