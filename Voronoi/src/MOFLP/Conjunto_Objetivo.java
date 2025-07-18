package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Conjunto_Objetivo {

    private HashMap<Point,Double> C;

    private HashMap<Point, LinkedList<Point>> grafo;

    private ArrayList<Point> sites;

    public Conjunto_Objetivo(HashMap<Point, LinkedList<Point>> grafo, ArrayList<Point> sites) {
        this.grafo = grafo;
        this.sites = sites;
        C = new HashMap<>();
    }

    public double evalPoint(Point p){
        if(C.containsKey(p)) return C.get(p);

        double min = Double.MAX_VALUE;
        for (Point site :sites){
            min = Math.min(min, p.distanceTo(site));
        }
        C.put(p,min);
        return min;
    }

    public void addPoint(Point p, Double d){
        C.put(p,d);
    }
}
