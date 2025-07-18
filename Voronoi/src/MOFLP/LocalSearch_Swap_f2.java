package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;


public class LocalSearch_Swap_f2 implements LocalSearch {



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

    private Comparator<Point> comp_descending = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            if(!C.containsKey(o1)) C.put(o1,pointEvaluator(o1));
            if(!C.containsKey(o2)) C.put(o2,pointEvaluator(o2));
            double o1v = C.get(o1);
            double o2v = C.get(o2);
            //Ordenamos de menor  a mayor
            return Double.compare(o1v,o2v);
        }
    };




    public LocalSearch_Swap_f2(HashMap<Point, LinkedList<Point>> grafo, int p, double d, ArrayList<Point> sites, long seed) {

        this.sites = sites;
        this.p = p;
        D = d;
        C=new HashMap<>();
        for (Point vp : grafo.keySet()) {//O(n²)
            double value = pointEvaluator(vp); //O(n)
            C.put(vp,value);
        }
        random=new Random(seed);

    }


    public Solucion improveSolution(Solucion sol) {
        this.solution=sol;
        solution.sort(comp_descending); //O(plogp) <<<<< O(n)
        ArrayList<Point> puntos_factibles = factibles(solution); //O(n)
        double valor_solucion = C.get(solution.getFacility(solution.size()-1));
        boolean flag_cambia = true;
        while(flag_cambia && solution.size()==p){//O(n²)
            //FIRST-IMPROVEMENT
            //Función de coste para fase de mejora  =  Maximizar la minima distancia
            //Misma función que la objetivo
            Point peor = solution.removeFacility(solution.size()-1);
            flag_cambia=false;
            Collections.shuffle(puntos_factibles);
            for(Point k : puntos_factibles){
                if(C.get(k)>valor_solucion){
                    solution.addFacility(k);
                    flag_cambia=true;
                    solution.sort(comp_descending);
                    valor_solucion = C.get(solution.getFacility(solution.size()-1));
                    puntos_factibles = factibles(solution);
                    break;
                }
            }
            if(!flag_cambia) solution.addFacility(peor);
        }

        return solution;
    }

    private ArrayList<Point> factibles(Solucion solucion){
        ArrayList<Point> factibles = new ArrayList<>();
        for (Point p : C.keySet()) {
            boolean factible =  true;
            for (Point p_sol : solucion){
                if(p.distanceTo(p_sol)<D){
                    factible=false;
                    break;
                }
            }
            if(factible) factibles.add(p);
        }
        return factibles;
    }

    private double pointEvaluator(Point p){
        double sum = 0;
        for (Point site :sites){
            sum = p.dist2(site);
        }
        return sum;
    }





}
