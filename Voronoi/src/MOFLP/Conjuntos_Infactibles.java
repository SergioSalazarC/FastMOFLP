package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;

public class Conjuntos_Infactibles {

    private HashMap<Point, Set<Point>> mem;
    private double D;


    public Conjuntos_Infactibles(Set<Point> posible_set, double D) {
        this.D=D;
        mem = new HashMap<>();

        for(Point p : posible_set){
            HashSet<Point> p_set = new HashSet<>();
            for(Point s : posible_set){
                if(p.distanceTo(s)<D)p_set.add(s);
            }
            mem.put(p,p_set);
        }
    }

    public Set<Point> getInfactibles(Point p){
        return mem.get(p);
    }

    public Set<Point> getAllPoints(){
        return this.mem.keySet();
    }

    public void addPoint(Point p){
            HashSet<Point> p_set = new HashSet<>();
            for(Point s : mem.keySet()){
                if(p.distanceTo(s)<D){
                    p_set.add(s);
                    mem.get(s).add(p);
                }
            }
            p_set.add(p);
            mem.put(p,p_set);

    }
    

}
