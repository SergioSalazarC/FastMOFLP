package MOFLP;

import org.ajwerner.voronoi.Point;

import java.util.*;

import static java.util.Collections.*;

public class Solucion implements Iterable<Point>{


    private ArrayList<Point> facilities;

    private HashSet<Point> factibles;
    private HashMap<Point,Integer> infactibles;
    private int p;

    private double D;

    private Conjuntos_Infactibles fs;





    public Solucion(int p, double D, Conjuntos_Infactibles fs) {
        facilities=new ArrayList<>(p*2);
        factibles = new HashSet<>();
        infactibles = new HashMap<>();
        for(Point point : fs.getAllPoints()) factibles.add(point);
        this.D = D;
        this.fs = fs;
        this.p=p;
    }

    public Solucion(Solucion s) {
        this.D = s.D;
        this.fs = s.fs;
        this.p=s.p;

        facilities= (ArrayList<Point>) s.facilities.clone();
        factibles = (HashSet<Point>) s.factibles.clone();
        infactibles = (HashMap<Point, Integer>) s.infactibles.clone();

    }



    public void sort(Comparator<Point> c){
        this.facilities.sort(c);
    }


    public Point getFacility(int index) {
        return facilities.get(index);
    }

    public Point removeFacility(int index) {
        Point ret = facilities.remove(index);
        add_Factibles(ret);
        return ret;
    }

    public void shuffleFacilities(){
        shuffle(this.facilities);
    }

    public boolean addFacility(Point f){
        if(fs.getInfactibles(f)==null) fs.addPoint(f);
        if(facilities.size() < p && facilities.add(f)){
            remove_Infactibles(f);
            return true;
        }
        else return false;
    }

    public int size(){
        return this.facilities.size();
    }

    public boolean check(double D){
        if(this.size()!=p) return false;
        for (int i = 0; i < p; i++) {
            for (int j = i+1; j < p; j++) {
                if(facilities.get(i).distanceTo(facilities.get(j))<D) return false;
            }
        }
        return true;
    }

    public double eval_check(ArrayList<Point> sites, double D){
        if(this.size()!=p) return 0;
        if(!check(D)) return 0;
        return eval_nocheck(sites);
    }

    public double eval_nocheck(ArrayList<Point> sites){
        if(this.size()!=p) return 0;
        double min = Double.MAX_VALUE;

        for (Point fac : facilities) {
            for (Point site : sites) {
                double dist = fac.distanceTo(site);
                min = Math.min(min,dist);
            }
        }

        return min;
    }

    @Override
    public int hashCode() {
        long acum = 0;
        for(Point p :  this.facilities){
            acum+= p.hashCode();
        }
        return Long.hashCode(acum);
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(this.getClass())) return false;
        Solucion s = (Solucion) obj;

        boolean si = true;

        for(Point p : this.facilities){
            boolean flag=false;
            for(Point q : s.facilities){
                if(p.equals(q)){
                    flag=true;
                    break;
                }
            }
            if(!flag){
                si=false;
                break;
            }
        }
        return si;

    }

    @Override
    public String toString() {
        StringBuilder aux = new StringBuilder("Solution: ");
        for(Point p : facilities) aux.append(p.toString()).append(" ");
        return aux.toString();
    }

    @Override
    public Iterator<Point> iterator() {
        return facilities.iterator();
    }

    private void remove_Infactibles(Point added){
        //Si eliminamos infactibles es porque añadimos nuevos puntos a la solucion
        for(Point p : fs.getInfactibles(added)){
            factibles.remove(p);
            if(infactibles.containsKey(p)) infactibles.put(p,infactibles.get(p)+1);
            else infactibles.put(p,1);
        }
    }

    private HashSet<Point> add_Factibles(Point remove){
        Set<Point> rem_inf = fs.getInfactibles(remove);
        for(Point p : rem_inf){
            if(infactibles.containsKey(p)){
                if(infactibles.get(p)==1){
                    factibles.add(p);
                    infactibles.remove(p);
                }
                else{
                    infactibles.put(p,infactibles.get(p)-1);
                }
            }
            else{
                factibles.add(p);
            }

        }
        return factibles;
    }


    public HashSet<Point> getFactibles() {
        return factibles;
    }

    public int factiblesSize(){ return factibles.size();}

    public Point randomFactible(){
        Random r = new Random();
        int aux = r.nextInt(factiblesSize());
        Iterator<Point> it = factibles.iterator();
        Point p=it.next();
        for (int i = 0; i <aux-1; i++) {
            p=it.next();
        }
        return p;

    }

    public boolean isFactible(Point p){
        return this.factibles.contains(p);
    }

}
