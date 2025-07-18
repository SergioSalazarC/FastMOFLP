package MOFLP;

import DirectSearchMethods.MDS_LocalSearch;
import DirectSearchMethods.Rosenbrock_LocalSearch;
import DirectSearchMethods.Simplex_LocalSearch;
import DirectSearchMethods.SolisWets_LocalSearch;
import org.ajwerner.voronoi.Point;

import java.util.*;

public  class GVNS {

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

    //Busquedas Locales para el GVNS;
    private LocalSearch[] ls = new LocalSearch[2];

    private Conjuntos_Infactibles fs;


    public GVNS(HashMap<Point, LinkedList<Point>> grafo, ArrayList<Point> sites, int p, double D, Conjuntos_Infactibles fs, Conjunto_Objetivo C) {
        this.grafo = grafo;
        this.sites = sites;
        this.p = p;
        this.D = D;
        this.C = C;
        this.fs =fs;
    }

    public Solucion execute(Solucion solucion, int localSearch1, int localSearch2, int kmax ){


        if(localSearch1 == 1) ls[0] = new LocalSearch_Swap_fObj_BI(grafo,p,D,sites,fs,(long) (Math.random()*10000000),C);
        else if(localSearch1==2) ls[0] = new LocalSearch_Swap_fObj_FI(grafo,p,D,sites,fs,(long) (Math.random()*10000000));
        else ls[0] = new LastPointImprover(grafo,sites,D,fs,C);

        if(localSearch2 == 1) ls[1] = new LocalSearch_Swap_fObj_BI(grafo,p,D,sites,fs,(long) (Math.random()*10000000),C);
        else if(localSearch2==2) ls[1] = new LocalSearch_Swap_fObj_FI(grafo,p,D,sites,fs,(long) (Math.random()*10000000));
        else if(localSearch2==3) ls[1] = new LastPointImprover(grafo,sites,D,fs,C);
        else if(localSearch2==4) ls[1] = new MDS_LocalSearch(grafo,p,D,sites,fs,(long) (Math.random()*10000000));
        else if(localSearch2==5) ls[1] = new Rosenbrock_LocalSearch(grafo,p,D,sites,fs,(long) (Math.random()*10000000));
        else if(localSearch2==6) ls[1] = new Simplex_LocalSearch(grafo,p,D,sites,fs,(long) (Math.random()*10000000));
        else if(localSearch2==7) ls[1] = new SolisWets_LocalSearch(grafo,p,D,sites,fs,(long) (Math.random()*10000000));




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

            /*
            generado = ls[0].improveSolution(generado);
             */
            double ev_actual = generado.eval_nocheck(sites);

            int counter  = 0;

            while (ev_actual!=0){
                double ev_it;
                Solucion aux;
                if(counter%2==0){
                    aux = ls[0].improveSolution(generado);
                    ev_it = generado.eval_nocheck(sites);
                }
                else{
                    aux = ls[1].improveSolution(generado);
                    ev_it = generado.eval_nocheck(sites);

                }
                if(ev_it<=ev_actual){
                    if(counter==1)break;
                }
                else{
                    ev_actual=ev_it;
                    generado=aux;
                    counter=-1;
                }
                counter++;
            }



            if(ev_actual==best) vecesshake++;
            else vecesshake=0;

            if(ev_actual >= best && !visitdos.contains(generado)){
                best = ev_actual;
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
