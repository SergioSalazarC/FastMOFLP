package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;


public class LastPointImprover implements LocalSearch{
    static double EPS = 1e-9;

    static Conjunto_Objetivo C;


    private Comparator<Point> comp_descending = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            double o1v = C.evalPoint(o1);
            double o2v = C.evalPoint(o2);
            //Ordenamos de mayor a menor
            return Double.compare(o2v,o1v);
        }
    };

    private class Segment{

        Point p1;
        Point p2;
        double A;
        double B;
        double C;

        double segDist;

        //El segmento de p1 a p2 pertenece a la recta Ax+By+C=0

        public Segment(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;

            segDist = p1.distanceTo(p2);
            A = p1.y - p2.y;
            B = p2.x - p1.x;
            C = -A*p1.x - B*p1.y;
        }

        public boolean isInSegment(Point q){
            return (Math.abs(A*q.x+B*q.y+C)<EPS && q.distanceTo(p1)<= segDist+EPS && q.distanceTo(p2)<= segDist+EPS);
        }

        public boolean isVertical(){
            return Math.abs(B)<EPS;
        }

    }

    private class Circle{
        Point centre;
        double radius;

        double D;
        double E;
        double F;

        //La ecuacion de la circunferencia es x²+y²+Dx+Ey+F=0


        public Circle(Point centre, double radius) {
            this.centre = centre;
            this.radius = radius;

            D=-2*centre.x;
            E=-2*centre.y;
            F=centre.x*centre.x+ centre.y* centre.y-radius*radius;
        }

        public Point segmentCut(Segment s){
            if(s.isVertical()){
                double x = -s.C/s.A;
                double A=1;
                double B=E;
                double C = x*x+D*x+F;
                double DELTA = B*B-4*A*C;

                double y1 = (-B+Math.sqrt(DELTA))/(2*A);
                double y2 = (-B-Math.sqrt(DELTA))/(2*A);

                Point p1 = new Point(x,y1);
                Point p2 = new Point(x,y2);

                if(p1==null && p2 == null || DELTA<=0){
                    System.err.println("ALGO SE HA ROTO EN EL CORTE CIRCULO RECTA");
                }

                double diststop1 = p1.distanceTo(s.p1)+p1.distanceTo(s.p2);
                double diststop2 = p2.distanceTo(s.p1)+p2.distanceTo(s.p2);

                if(diststop1<diststop2)return p1;
                else return p2;





            }
            else{
                double m = -s.A/s.B;
                double n = -s.C/s.B;
                //Resolvemos la ecuacion Ax²+Bx+C=0 resultante de despejar
                double A=(m*m)+1;
                double B=(D)+(2*m*n)+(E*m);
                double C = (n*n)+(E*n)+(F);

                //Por como está implementado el uso de esta función siempre debe tener dos soluciones

                double DELTA = B*B-4*A*C;
                double x1 = (-B+Math.sqrt(DELTA))/(2*A);
                double x2 = (-B-Math.sqrt(DELTA))/(2*A);

                double y1 = m*x1+n;
                double y2 = m*x2+n;

                Point p1 = new Point(x1,y1);
                Point p2 = new Point(x2,y2);

                if(p1==null && p2 == null || DELTA<=0){
                    System.err.println("ALGO SE HA ROTO EN EL CORTE CIRCULO RECTA");
                }


                double diststop1 = p1.distanceTo(s.p1)+p1.distanceTo(s.p2);
                double diststop2 = p2.distanceTo(s.p1)+p2.distanceTo(s.p2);

                if(diststop1<diststop2)return p1;
                else return p2;


            }
        }
    }

    //Grafo de Voronoi
    private HashMap<Point, LinkedList<Point>> grafo;

    private Conjuntos_Infactibles fs;

    private double D;

    //Sites
    private ArrayList<Point> sites;



    public LastPointImprover(HashMap<Point, LinkedList<Point>> grafo, ArrayList<Point> sites, double D, Conjuntos_Infactibles fs, Conjunto_Objetivo C) {
        this.grafo = grafo;
        this.sites = sites;
        this.D = D;
        this.fs =fs;
        this.C=C;
    }

    public Solucion improveSolution(Solucion sol){
        sol.sort(comp_descending);
        // HAY QUE ASEGURAR QUE LOS PUNTOS ESTEN ORDENADOS POR VALOR
        Point p = sol.removeFacility(sol.size()-1);

        LinkedList<Point> adyacentes = grafo.get(p);

        if(adyacentes==null){
            sol.addFacility(p);
            return sol;
        }
        else{
            double vact = C.evalPoint(p);
            Point best = p;
            boolean flag = false;

            for(Point pos : adyacentes){
                double aux = C.evalPoint(pos);
                if(aux>vact){
                    flag=true;
                    best=pos;
                    vact = aux;
                }
            }

            if(!flag){
                sol.addFacility(p);
                return sol;
            }
            else{
                Point s = best;
                if(sol.isFactible(s)){
                    sol.addFacility(s);
                    return improveSolution(sol);
                }
                else{
                    Point better = findCenterInfactible(p,s,D,sol);

                    if(better==null || better.equals(p)){
                        sol.addFacility(p);
                        return sol;
                    }
                    sol.addFacility(better);
                    return sol;
                }
            }

        }
    }





    private Point findCenterInfactible(Point fact, Point infact, double D, Solucion solucion){

        double distcercano = Double.MAX_VALUE;
        Point mascercano = new Point(0,0);
        Point centreCircle = new Point(0,0);



        for(Point s : solucion){
            if(infact.distanceTo(s)<D){
                Circle c = new Circle(s,D);
                Segment seg = new Segment(fact,infact);
                Point cut = c.segmentCut(seg);
                if(cut==null) return fact;
                if(cut.distanceTo(fact)<distcercano){
                    mascercano=cut;
                    distcercano=cut.distanceTo(fact);
                    centreCircle = s;
                }
            }
        }


        Point vector = new Point((mascercano.x-centreCircle.x)*1E-10,(mascercano.y-centreCircle.y)*1E-10);

        //ERRORES DE DECIMALES
        Point posible = new Point(mascercano.x+vector.x,mascercano.y+vector.y);

        fs.addPoint(posible);


        return posible;
    }


}
