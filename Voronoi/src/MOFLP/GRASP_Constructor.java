package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;


public class GRASP_Constructor {

    private class Pair implements Comparable {
        Point p;
        Double value;

        public Pair(Point p, Double value) {
            this.p = p;
            this.value = value;
        }

        @Override
        public int compareTo(Object o) {
            Pair i = (Pair) o;
            //QUEREMOS ORDENAR DE MAYOR A MENOR
            return Double.compare(i.value,this.value);
        }
    }
    

    //Parametro alpha del grasp para el tamaño de la RCL
    private double alpha;
    //Grafo de Voronoi
    private HashMap<Point, LinkedList<Point>> grafo;
    //Sites
    private  ArrayList<Point> sites;
    //Cantidad de facilities a colocar
    private int p;
    //Distancia entre facilities
    private double D;
    //Coste para cada candidato
    private Conjunto_Objetivo C;

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

    private Random random;
    
    
    
    


    public GRASP_Constructor(HashMap<Point, LinkedList<Point>> grafo, int p, double D, ArrayList<Point> sites, double alpha, Conjuntos_Infactibles fs, long seed, Conjunto_Objetivo C) {
        this.alpha = alpha;
        this.grafo = grafo;
        this.sites = sites;
        this.p = p;
        this.D = D;
        this.C = C;
        this.fs =fs;
        random=new Random(seed);

    }

    public Solucion generateSolution() {

        ArrayList<Point> candidatos = new ArrayList<>(this.grafo.keySet());
        Collections.sort(candidatos,comp_descending);//O(nlogn)
        //FASE CONSTRUCTIVA ==> O(nlogn)
        int Lsize = grafo.size();
        int randomP = randomLT(Lsize);
        Point eliminado = candidatos.remove(randomP);
        Solucion solution=new Solucion(p,D,fs);
        solution.addFacility(eliminado);
        eliminarInfactiblesCandidatos(eliminado, candidatos);


        while (solution.size() < p && !candidatos.isEmpty()) {
            double RCLumbral;
            if(alpha==-1){
                RCLumbral = random.nextDouble();
            }
            else{
                double CMax = C.evalPoint(candidatos.get(0));
                double CMin = C.evalPoint(candidatos.get(candidatos.size() - 1));
                RCLumbral = (CMin + alpha * (CMax - CMin));
            }


            int index = binarySearch(candidatos,RCLumbral); //O(log n)

            randomP = randomLT(index);
            eliminado = candidatos.remove(randomP);
            solution.addFacility(eliminado);
            eliminarInfactiblesCandidatos(eliminado , candidatos);


        }
        return solution;
    }

    

    private int randomLT(int lt){
        double a = random.nextDouble()*lt;
        return (int) a;
    }

    private void eliminarInfactiblesCandidatos(Point p, ArrayList<Point> cand) {
        Iterator<Point> itr = cand.iterator();
        while(itr.hasNext()){
            Point point = itr.next();
            if(point.distanceTo(p)<D){
                itr.remove();
            }
        }
    }

    private int binarySearch(ArrayList<Point> candidatos, double umbral){
        //Puntos ordenados de mayor a menor!!
        int l = 0;
        int r = candidatos.size()-1;

        if(umbral > C.evalPoint(candidatos.get(l))) return l;
        if(umbral < C.evalPoint(candidatos.get(r))) return r+1;

        while(r-l>1){
            int m = (l+r)/2;
            if(umbral == C.evalPoint(candidatos.get(m))) return m;
            if(umbral > C.evalPoint(candidatos.get(m))){
                r=m;
            }
            else{
                l=m;
            }

        }

        if(umbral >= C.evalPoint(candidatos.get(l))) return l;
        if(C.evalPoint(candidatos.get(l)) > umbral && umbral >= C.evalPoint(candidatos.get(r))) return r;
        if(C.evalPoint(candidatos.get(r)) > umbral) return r+1;

        System.out.println("ERROR");

        return r+1;


    }



}
