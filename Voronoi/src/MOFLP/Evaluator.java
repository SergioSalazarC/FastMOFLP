package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.ArrayList;
import java.util.TreeMap;

public class Evaluator {

    private static double EPS = 1E-9;


    //Cantidad de sites
    private int n;
    //Localizacion de los sites
    private ArrayList<Point> sites;
    //facility que tiene la peor localizacion
    private Point worstFacility;
    //site que tiene la peor distancia
    private Point worstSite;
    //valor de la solucion
    private double L;
    //distancia entre las facilities
    private double D;
    //
    private TreeMap<Point, Double> f_1;





    public Evaluator(ArrayList<Point> sites, double D ) {
        this.n = sites.size();
        this.sites = sites;
        this.D=D;
    }

    public Point getWorstFacility() {
        return worstFacility;
    }

    public Point getWorstSite() {
        return worstSite;
    }





    public boolean check(ArrayList<Point> solution){
        int p = solution.size();
        for (int i = 0; i < p; i++) {
            if(Math.abs(solution.get(i).x) > 1+EPS || Math.abs(solution.get(i).y) > 1+EPS ) return false;
            for (int j = i+1; j < p; j++) {
                if(solution.get(i).dist2(solution.get(j))<D*D) return false;
            }
        }
        return true;
    }



    public double eval(ArrayList<Point> solution, int p){
        if(p!=solution.size()) return 0;
        double min = Double.MAX_VALUE;
        if(!check(solution)) return 0;

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < n; j++) {
                if(min>solution.get(i).dist2(sites.get(j))){
                    min = solution.get(i).dist2(sites.get(j));
                    worstFacility=solution.get(i);
                    worstSite=sites.get(j);
                }
            }
        }
        L=min;
        return min;
    }

    public double eval_nocheck(ArrayList<Point> solution){
        int p = solution.size();
        double min = Double.MAX_VALUE;

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < n; j++) {
                if(min>solution.get(i).dist2(sites.get(j))){
                    min = solution.get(i).dist2(sites.get(j));
                    worstFacility=solution.get(i);
                    worstSite=sites.get(j);
                }
            }
        }
        L=min;
        return min;
    }



}
