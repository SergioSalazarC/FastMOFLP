package MOFLP;

import org.ajwerner.voronoi.Point;

import java.awt.*;
import java.util.*;

public class Greedy {

    //Grafo de Voronoi
    private HashMap<Point, LinkedList<Point>> grafo;
    //Sites
    private ArrayList<Point> sites;
    //Cantidad de facilities a colocar
    private int p;
    //Distancia entre facilities
    private double D;

    private class Pair implements Comparable{
        Point p;
        Double value;

        public Pair(Point p, Double value) {
            this.p = p;
            this.value = value;
        }

        @Override
        public int compareTo(Object o) {
            Pair i = (Pair) o;
            return Double.compare(this.value,i.value);
        }
    }

    public Greedy(HashMap<Point, LinkedList<Point>> grafo, int p, double D, ArrayList<Point> sites) {
        this.grafo = grafo;
        this.p=p;
        this.D=D;
        this.sites=sites;
    }

    private double voronoiPointevaluator(Point p){
        double min = Double.MAX_VALUE;
        for (Point site :sites){
            min = Math.min(min, p.dist2(site));
        }
        return min;
    }

    public ArrayList<Point> generateSolution(){
        return generateSolution(false,false);
    }
    public ArrayList<Point> generateSolution(boolean showPoints, boolean showCircleAreas){
        LinkedList<Pair> aux = new LinkedList<>();
        for (Point p : grafo.keySet()){
            aux.add(new Pair(p,voronoiPointevaluator(p)));
        }
        Collections.sort(aux,Collections.reverseOrder());

        ArrayList<Point> solution = new ArrayList<>();
        while(!aux.isEmpty() && solution.size()<p){
            Pair s = aux.removeFirst();
            boolean factible = true;
            for (Point p : solution){
                if(p.dist2(s.p)<D*D) factible = false;
            }
            if(factible){
                solution.add(s.p);
                if(showPoints){
                }
                if(showCircleAreas){
                    Color c = new Color(255, 154, 133, 127);
                }
            }
        }
        if(solution.size()==p) return solution;
        else return null;

    }
}
