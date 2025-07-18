package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;


public class LocalSearch_Swap_fObj_BI implements LocalSearch {

    //Sites
    private  ArrayList<Point> sites;
    //Cantidad de facilities a colocar
    private int p;
    //Distancia entre facilities
    private double D;

    //Variable para la solución.
    private Solucion solution;
    //Coste para cada candidato
    private Conjunto_Objetivo C;

    private Random random;

    private Conjuntos_Infactibles fs;

    private Comparator<Point> comp_descending = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            double o1v = C.evalPoint(o1);
            double o2v = C.evalPoint(o2);
            //Ordenamos de mayor a menor
            return Double.compare(o2v,o1v);
        }
    };




    public LocalSearch_Swap_fObj_BI(HashMap<Point, LinkedList<Point>> grafo, int p, double d, ArrayList<Point> sites, Conjuntos_Infactibles fs , long seed, Conjunto_Objetivo C) {

        this.sites = sites;
        this.p = p;
        D = d;
        this.C = C;
        random=new Random(seed);
        this.fs = fs;

    }


    public Solucion improveSolution(Solucion sol) {
        this.solution=sol;
        solution.sort(comp_descending); //O(plogp) <<<<< O(n)
        double valor_solucion = C.evalPoint(solution.getFacility(solution.size()-1));
        boolean flag_cambia = true;
        while(flag_cambia && solution.size()==p){//O(n²)
            //BEST-IMPROVEMENT
            //Función de coste para fase de mejora  =  Maximizar la minima distancia
            //Misma función que la objetivo
            Point peor = solution.removeFacility(solution.size()-1);
            flag_cambia=false;
            double max = valor_solucion;
            Point best = null;
            for(Point k : solution.getFactibles()){
                if(C.evalPoint(k)>max ){
                    best = k;
                    max = C.evalPoint(k);
                    flag_cambia=true;
                }
            }

            if(flag_cambia){
                solution.addFacility(best);
                solution.sort(comp_descending);
                valor_solucion = C.evalPoint(solution.getFacility(solution.size()-1));
            }
            else solution.addFacility(peor);
        }

        return solution;
    }








}
