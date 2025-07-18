package org.ajwerner.voronoi;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by ajwerner on 12/23/13.
 */
public class Voronoi {
    public static final double EPS =1E-10;

    public static final double DELTA = 5;
    public static final double MIN_DRAW_DIM = -5;
    public static final double MAX_DRAW_DIM = 5;
    // Ghetto but just for drawing stuff
    private static final double MAX_DIM = 10;
    private static final double MIN_DIM = -10;
    private double sweepLoc;
    private final ArrayList<Point> sites;
    private final ArrayList<VoronoiEdge> edgeList;
    private final HashSet<BreakPoint> breakPoints;
    private final TreeMap<ArcKey, CircleEvent> arcs;
    private final TreeSet<Event> events;

    //Grafo de Voronoy
    private HashMap<Point, LinkedList<Point>> grafo;


    public HashMap<Point, LinkedList<Point>> getGrafo() {
        return grafo;
    }


    public double getSweepLoc() {
        return sweepLoc;
    }

    /*
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length==0) {
            //int N = Integer.parseInt(args[0]);
            ArrayList<Point> sites = new ArrayList<Point>();

            Random rnd = new Random();
            for (int i = 0; i < N; i++) {
                sites.add(new Point(rnd.nextDouble(), rnd.nextDouble()));
            }




            Locale.setDefault(Locale.ENGLISH);

            File file = new File("/home/sergio/TFG-Informatica_Github/Voronoi/Kalczynski-Drezner_data_1000.txt");
            Scanner in = new Scanner(file);
            for (int i = 0; i < 10; i++) {
                sites.add(new Point(in.nextDouble(),in.nextDouble()));
            }



            sites.add(new FPoint(0.5,0.5));
            sites.add(new FPoint(0.5,0.9));
            sites.add(new FPoint(0.9,0.5));
            sites.add(new FPoint(0.8,0.3));
            sites.add(new FPoint(0.0144, 0.032));
            sites.add(new FPoint(31.0/45.0,0.7111));





            StdDraw.setCanvasSize(600, 600);
            StdDraw.setScale(-.1, 1.1);
            long a = System.currentTimeMillis();
            //Voronoi v = new Voronoi(sites, false);
            long b = System.currentTimeMillis();
            System.out.println(b-a);
            //v.show();
        }
        else {
            int numTrials = 5;
            System.out.println("         N:   \ttime (s)");
            int[] Ns = {50000, 100000, 200000, 400000, 800000, 1600000, 3200000};
            for (int n : Ns) {
                double[] res = new double[numTrials];
                for (int i = 0; i < numTrials; i++) {
                    res[i] = randomTrial(n);
                }
                System.out.printf("%10d:\t%-5.6f +/- %f \n", n, StdStats.mean(res), StdStats.stddev(res)/Math.sqrt(numTrials));
            }
        }
    }
    */

    private static double randomTrial(int N) {
        Random rnd = new Random();
        ArrayList<Point> sites = new ArrayList<Point>();
        double stop, start;
        sites.clear();
        for (int i = 0; i < N; i++) {
            sites.add(new Point(rnd.nextDouble(), rnd.nextDouble()));
        }
        // Voronoi v = new Voronoi(sites);

        return 0;
    }

    public Voronoi(ArrayList<Point> sites) {
        this(sites, false, 0.01);
    }

    public Voronoi(ArrayList<Point> sites, double beta) {
        this(sites, false, beta);
    }



    public Voronoi(ArrayList<Point> sites, boolean animate, double beta) {
        //Beta es la finura del diagrama de Voronoi
        // initialize data structures;
        this.sites = sites;
        grafo = new HashMap<>();
        edgeList = new ArrayList<VoronoiEdge>(sites.size());
        events = new TreeSet<Event>();
        breakPoints = new HashSet<BreakPoint>();
        arcs = new TreeMap<ArcKey, CircleEvent>();

        for (Point site : sites) {
            if ((site.x > MAX_DIM || site.x < MIN_DIM) || (site.y > MAX_DIM || site.y < MIN_DIM))
                throw new RuntimeException(String.format(
                    "Invalid site in input, sites must be between %f and %f", MIN_DIM, MAX_DIM ));
            events.add(new Event(site));
        }
        sweepLoc = MAX_DIM;
        do {
            Event cur = events.pollFirst();
            sweepLoc = cur.p.y;
            if (animate) this.draw();
            if (cur.getClass() == Event.class) {
                handleSiteEvent(cur);
            }
            else {
                CircleEvent ce = (CircleEvent) cur;
                handleCircleEvent(ce);
            }
        } while ((events.size() > 0));

        this.sweepLoc = MIN_DIM; // hack to draw negative infinite points
        for (BreakPoint bp : breakPoints) {
            bp.finish();
        }

        //Construcción del grafo:
        //Guardamos en esquinas las esquinas del cuadrado que deben ser nodos del diagrama
        //Queremos incluir los bordes del cuadrado en el grafo
        //Hacemos 4 listas que ordenaremos para los bordes:
        //
        //      2----top------3
        //      |             |
        //     left          right
        //      |             |
        //      0---bottom----1
        //
        Point[] esquinas = new Point[4];

        esquinas[0]=new Point(0,0);
        esquinas[1]=new Point(1,0);
        esquinas[2]=new Point(0,1);
        esquinas[3]=new Point(1,1);
        ArrayList<Point> top = new ArrayList<>();
        top.add(esquinas[2]);
        top.add(esquinas[3]);
        ArrayList<Point> bottom = new ArrayList<>();
        bottom.add(esquinas[0]);
        bottom.add(esquinas[1]);
        ArrayList<Point> left = new ArrayList<>();
        left.add(esquinas[0]);
        left.add(esquinas[2]);
        ArrayList<Point> right = new ArrayList<>();
        right.add(esquinas[1]);
        right.add(esquinas[3]);

        for(VoronoiEdge v_e : this.edgeList){
            Point p1 = v_e.p1;
            Point p2 = v_e.p2;
            if(!p1.inSquare(1) && !p2.inSquare(1)) continue;
            Point a=p1;
            Point b=p2;
            if(!p1.inSquare(1)){
                a = new Point(p1,p2,1);
            }

            if(!p2.inSquare(1)){
                b = new Point(p2,p1,1);
            }
            for (int i = 0; i < 2; i++) {
                Point aux;
                if(i==0)aux=a;
                else aux=b;

                if(aux.y==1){
                    top.add(aux);
                }
                else if(aux.y==0){
                    bottom.add(aux);
                }
                else if(aux.x==0){
                    left.add(aux);
                }
                else if(aux.x==1){
                    right.add(aux);
                }
            }
            /*
            if(!grafo.containsKey(a)){
                grafo.put(a,new LinkedList<>());
            }
            if(!grafo.containsKey(b)){
                grafo.put(b,new LinkedList<>());
            }

             */

            Point v_atob_part = new Point((b.x-a.x),(b.y-a.y));
            v_atob_part = new Point(beta*v_atob_part.x/modulo(v_atob_part),beta*v_atob_part.y/modulo(v_atob_part));
            /*
            El casting hace un truncamiento, es lo deseado ya que si por ejemplo es 3.8 veces
            la distancia solo se puede hacer 3 veces completa.
             */
            int itr_arista = (int) (a.distanceTo(b)/beta);

            Point a2 = a;
            ArrayList<Point> aristaPartida = new ArrayList<>();
            for (int i = 0; i < itr_arista; i++){
                aristaPartida.add(a2);
                a2 = new Point(a2.x+v_atob_part.x,a2.y+v_atob_part.y);
            }
            if(aristaPartida.size()==0)aristaPartida.add(a);
            aristaPartida.add(b);

            for (int i = 0; i < aristaPartida.size()-1; i++) {
                Point k1 = aristaPartida.get(i);
                Point k2 = aristaPartida.get(i+1);
                if(!grafo.containsKey(k1)){
                    grafo.put(k1,new LinkedList<>());
                }
                if(!grafo.containsKey(k2)){
                    grafo.put(k2,new LinkedList<>());
                }
                if(k1.y==1){
                    top.add(k1);
                }
                else if(k1.y==0){
                    bottom.add(k1);
                }
                else if(k1.x==0){
                    left.add(k1);
                }
                else if(k1.x==1){
                    right.add(k1);
                }

                grafo.get(k1).add(k2);
                grafo.get(k2).add(k1);

            }



        }


        for (int i = 0; i < 4; i++) {
            grafo.put(esquinas[i],new LinkedList<>());
        }

        //top y bottom se ordenan de izquierda a derecha
        Comparator coordenadaX = new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return Double.compare(o1.x,o2.x);
            }
        };
        Collections.sort(top,coordenadaX);
        Collections.sort(bottom,coordenadaX);

        //left y right de abajo a arriba
        Comparator<Point> coordenadaY = new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return Double.compare(o1.y,o2.y);
            }
        };
        Collections.sort(left, coordenadaY);
        Collections.sort(right,coordenadaY);

        for (int t = 0; t < 4; t++) {
            ArrayList<Point> lista=top;
            if(t==0) lista=top;
            if(t==1) lista=bottom;
            if(t==2) lista=left;
            if(t==3) lista=right;

            for (int j = 0; j < lista.size() - 1; j++) {
                Point a = lista.get(j);
                Point b = lista.get(j+1);
                Point v_atob_part = new Point((b.x-a.x),(b.y-a.y));
                v_atob_part = new Point(beta*v_atob_part.x/modulo(v_atob_part),beta*v_atob_part.y/modulo(v_atob_part));
                /*
                El casting hace un truncamiento, es lo deseado ya que si por ejemplo es 3.8 veces
                la distancia solo se puede hacer 3 veces completa.
                 */
                int itr_arista = (int) (a.distanceTo(b)/beta);

                Point a2 = a;

                ArrayList<Point> aristaPartida = new ArrayList<>();
                for (int i = 0; i < itr_arista; i++){
                    aristaPartida.add(a2);
                    a2 = new Point(a2.x+v_atob_part.x,a2.y+v_atob_part.y);
                }
                if(aristaPartida.size()==0)aristaPartida.add(a);
                aristaPartida.add(b);

                for (int i = 0; i < aristaPartida.size()-1; i++) {
                    Point k1 = aristaPartida.get(i);
                    Point k2 = aristaPartida.get(i+1);
                    if(!grafo.containsKey(k1)){
                        grafo.put(k1,new LinkedList<>());
                    }
                    if(!grafo.containsKey(k2)){
                        grafo.put(k2,new LinkedList<>());
                    }

                    grafo.get(k1).add(k2);
                    grafo.get(k2).add(k1);

                }

                //grafo.get(a).add(b);
                //grafo.get(a).add(b);
            }
        }




    }

    private void handleSiteEvent(Event cur) {
        // Deal with first point case
        if (arcs.size() == 0) {
            arcs.put(new Arc(cur.p, this), null);
            return;
        }

        // Find the arc above the site
        Map.Entry<ArcKey, CircleEvent> arcEntryAbove = arcs.floorEntry(new ArcQuery(cur.p));
        Arc arcAbove = (Arc) arcEntryAbove.getKey();

        // Deal with the degenerate case where the first two points are at the same y value
        if (arcs.size() == 0 && arcAbove.site.y == cur.p.y) {
            VoronoiEdge newEdge = new VoronoiEdge(arcAbove.site, cur.p);
            newEdge.p1 = new Point((cur.p.x + arcAbove.site.x)/2, Double.POSITIVE_INFINITY);
            BreakPoint newBreak = new BreakPoint(arcAbove.site, cur.p, newEdge, false, this);
            breakPoints.add(newBreak);
            this.edgeList.add(newEdge);
            Arc arcLeft = new Arc(null, newBreak, this);
            Arc arcRight = new Arc(newBreak, null, this);
            arcs.remove(arcAbove);
            arcs.put(arcLeft, null);
            arcs.put(arcRight, null);
            return;
        }

        // Remove the circle event associated with this arc if there is one
        CircleEvent falseCE = arcEntryAbove.getValue();
        if (falseCE != null) {
            events.remove(falseCE);
        }

        BreakPoint breakL = arcAbove.left;
        BreakPoint breakR = arcAbove.right;
        VoronoiEdge newEdge = new VoronoiEdge(arcAbove.site, cur.p);
        this.edgeList.add(newEdge);
        BreakPoint newBreakL = new BreakPoint(arcAbove.site, cur.p, newEdge, true, this);
        BreakPoint newBreakR = new BreakPoint(cur.p, arcAbove.site, newEdge, false, this);
        breakPoints.add(newBreakL);
        breakPoints.add(newBreakR);

        Arc arcLeft = new Arc(breakL, newBreakL, this);
        Arc center = new Arc(newBreakL, newBreakR, this);
        Arc arcRight = new Arc(newBreakR, breakR, this);

        arcs.remove(arcAbove);
        arcs.put(arcLeft, null);
        arcs.put(center, null);
        arcs.put(arcRight, null);

        checkForCircleEvent(arcLeft);
        checkForCircleEvent(arcRight);
    }

    private void handleCircleEvent(CircleEvent ce) {
        arcs.remove(ce.arc);
        ce.arc.left.finish(ce.vert);
        ce.arc.right.finish(ce.vert);
        breakPoints.remove(ce.arc.left);
        breakPoints.remove(ce.arc.right);

        Entry<ArcKey, CircleEvent> entryRight = arcs.higherEntry(ce.arc);
        Entry<ArcKey, CircleEvent> entryLeft = arcs.lowerEntry(ce.arc);
        Arc arcRight = null;
        Arc arcLeft = null;

        Point ceArcLeft = ce.arc.getLeft();
        boolean cocircularJunction = ce.arc.getRight().equals(ceArcLeft);

        if (entryRight != null) {
            arcRight = (Arc) entryRight.getKey();
            while (cocircularJunction && arcRight.getRight().equals(ceArcLeft)) {
                arcs.remove(arcRight);
                arcRight.left.finish(ce.vert);
                arcRight.right.finish(ce.vert);
                breakPoints.remove(arcRight.left);
                breakPoints.remove(arcRight.right);

                CircleEvent falseCe = entryRight.getValue();
                if (falseCe != null) {
                    events.remove(falseCe);
                }

                entryRight = arcs.higherEntry(arcRight);
                arcRight = (Arc) entryRight.getKey();
            }

            CircleEvent falseCe = entryRight.getValue();
            if (falseCe != null) {
                events.remove(falseCe);
                arcs.put(arcRight, null);
            }
        }
        if (entryLeft != null) {
            arcLeft = (Arc) entryLeft.getKey();
            while (cocircularJunction && arcLeft.getLeft().equals(ceArcLeft)) {
                arcs.remove(arcLeft);
                arcLeft.left.finish(ce.vert);
                arcLeft.right.finish(ce.vert);
                breakPoints.remove(arcLeft.left);
                breakPoints.remove(arcLeft.right);

                CircleEvent falseCe = entryLeft.getValue();
                if (falseCe != null) {
                    events.remove(falseCe);
                }

                entryLeft = arcs.lowerEntry(arcLeft);
                arcLeft = (Arc) entryLeft.getKey();
            }

            CircleEvent falseCe = entryLeft.getValue();
            if (falseCe != null) {
                events.remove(falseCe);
                arcs.put(arcLeft, null);
            }
        }

        VoronoiEdge e = new VoronoiEdge(arcLeft.right.s1, arcRight.left.s2);
        edgeList.add(e);

        // Here we're trying to figure out if the org.ajwerner.voronoi.Voronoi vertex
        // we've found is the left
        // or right point of the new edge.
        // If the edges being traces out by these two arcs take a right turn then we
        // know
        // that the vertex is going to be above the current point
        boolean turnsLeft = Point.ccw(arcLeft.right.edgeBegin, ce.p, arcRight.left.edgeBegin) == 1;
        // So if it turns left, we know the next vertex will be below this vertex
        // so if it's below and the slow is negative then this vertex is the left point
        boolean isLeftPoint = (turnsLeft) ? (e.m < 0) : (e.m > 0);
        if (isLeftPoint) {
            e.p1 = ce.vert;
        } else {
            e.p2 = ce.vert;
        }

        BreakPoint newBP = new BreakPoint(arcLeft.right.s1, arcRight.left.s2, e, !isLeftPoint, this);
        breakPoints.add(newBP);

        arcRight.left = newBP;
        arcLeft.right = newBP;

        checkForCircleEvent(arcLeft);
        checkForCircleEvent(arcRight);
    }

    private void checkForCircleEvent(Arc a) {
        Point circleCenter = a.checkCircle();
        if (circleCenter != null) {
            double radius = a.site.distanceTo(circleCenter);
            Point circleEventPoint = new Point(circleCenter.x, circleCenter.y - radius);
            CircleEvent ce = new CircleEvent(a, circleEventPoint, circleCenter);
            arcs.put(a, ce);
            events.add(ce);
        }
    }

    public void show() {
    }

    private void draw() {
    }


    private static boolean pertenece(Point a, Point b, Point c){
        if(a.x==b.x){
            if(a.x==c.x && ((a.y<=c.y && c.y<=b.y) || (b.y<=c.y && c.y<=a.y))) return true;
        }
        else{
            double m =(a.y-b.y)/(a.x-b.x);
            double n = a.y-m*a.x;
            if(c.y == m*c.x+n){
                double xm=Math.min(a.x,b.x);
                double xM=Math.max(a.x,b.x);
                double ym=Math.min(a.y,b.y);
                double yM=Math.max(a.y,b.y);

                if(xm<=c.x && c.x<=xM && ym <= c.y && c.y<=yM){
                    return true;
                }
            }
        }
        return false;
    }

    private static double modulo(Point a){
        return Math.sqrt(a.x*a.x + a.y*a.y);
    }

}

