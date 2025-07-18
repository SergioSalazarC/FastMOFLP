package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;

public class VNS {


    //Grafo de Voronoi
    private HashMap<Point, LinkedList<Point>> grafo;
    //Sites
    private ArrayList<Point> sites;
    //Cantidad de facilities a colocar
    private int p;
    //Distancia entre facilities
    private double D;
    //Coste para cada candidato
    private Conjunto_Objetivo C;

    //Busqueda Local para el VNS;
    private LocalSearch ls;

    private Conjuntos_Infactibles fs;



    public VNS(HashMap<Point, LinkedList<Point>> grafo, ArrayList<Point> sites, int p, double D, Conjuntos_Infactibles fs, Conjunto_Objetivo C) {
        this.grafo = grafo;
        this.sites = sites;
        this.p = p;
        this.D = D;
        this.C = C;
        this.fs =fs;
    }

    public Solucion execute(Solucion solucion, int localSearch, int kmax){

        if(localSearch == 1) ls = new LocalSearch_Swap_fObj_BI(grafo,p,D,sites,fs,(long) (Math.random()*10000000),C);
        else ls = new LocalSearch_Swap_fObj_FI(grafo,p,D,sites,fs,(long) (Math.random()*10000000));

        HashSet<Solucion> visitdos = new HashSet<>();
        visitdos.add(solucion);

        double best = solucion.eval_nocheck(sites);

        //La variable localSearch indica que busqueda local implementar
        Solucion mejor = new Solucion(solucion);


        int vecesshake = 0;


        for (int k = 1; k <= kmax; k++) {
            Solucion generado = new Solucion(mejor);
            generado = shake(generado,k,vecesshake);
            if(generado == null) continue;

            generado = ls.improveSolution(generado);

            double actual = generado.eval_nocheck(sites);
            if(actual==best) vecesshake++;
            else vecesshake=0;

            if(actual >= best && !visitdos.contains(generado)){
                best = actual;
                mejor = generado;
                visitdos.add(generado);
                k=0;
            }


        }
        return mejor;

    }


    public Solucion shake(Solucion solucion, int k, int veces){
        Solucion parcial = new Solucion(solucion);
        if(veces>=15){
            k--;
            parcial.removeFacility(parcial.size()-1);
        }

        parcial.shuffleFacilities();

        for (int i = 0; i < k; i++) {
            parcial.removeFacility(parcial.size()-1);
        }

        if(veces>=15) k++;

        for (int i = 0; i < k; i++) {
            if(parcial.factiblesSize()==0) return null;
            Point s = parcial.randomFactible();
            parcial.addFacility(s);
        }
        return parcial;


    }





}
