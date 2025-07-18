package org.ajwerner.voronoi;


import java.awt.*;

/**
 * Created by ajwerner on 12/23/13.
 */
public class Point implements Comparable<Point> {
    public final double x, y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p, Point s, double squareSize){
        //Devuelve un punto donde los infinitos se encuentran en el cuadrado de lado squareSize.
        //Si el punto p esta en el infinito es porque la arista p-s corta al cuadrado del dominio.
        //El punto que nos interesa por tanto es aquel que corta la arista del cuadrado.
        double l = squareSize;

        double m = (p.y-s.y)/(p.x-s.x);
        double n = p.y-m*p.x;
        if(p.x<0 && p.y<0){
            //Caben dos posibilidades
            double x1 = 0;
            double y1 = n;
            double x2 = -n/m;
            double y2 = 0;
            if(new Point(x1,y1).inSquare(1)){
                x=x1;
                y=y1;
            }
            else{
                x=x2;
                y=y2;
            }
        }
        else if(p.x<0 && p.y>squareSize){
            double x1 = 0;
            double y1 = n;
            double y2=squareSize;
            double x2=(y2-n)/m;
            if(new Point(x1,y1).inSquare(1)){
                x=x1;
                y=y1;
            }
            else{
                x=x2;
                y=y2;
            }
        }
        else if(p.x>squareSize && p.y<0){
            double x1=squareSize;
            double y1=m*x1+n;
            double x2 = -n/m;
            double y2 = 0;
            if(new Point(x1,y1).inSquare(1)){
                x=x1;
                y=y1;
            }
            else{
                x=x2;
                y=y2;
            }
        }
        else if(p.x>squareSize && p.y> squareSize){
            double x1=squareSize;
            double y1=m*x1+n;
            double y2=squareSize;
            double x2=(y2-n)/m;
            if(new Point(x1,y1).inSquare(1)){
                x=x1;
                y=y1;
            }
            else{
                x=x2;
                y=y2;
            }
        }
        else if(p.x<0){
            x=0;
            y=n;
        }
        else if(p.y<0){
            x=-n/m;
            y=0;
        }
        else if(p.x>squareSize){
            x=squareSize;
            y=m*x+n;
        }
        else if(p.y>squareSize){
            y=squareSize;
            x=(y-n)/m;
        }
        else{
            x=p.x;
            y=p.y;
        }


    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    @Override
    public int compareTo(Point o) {
        if ((this.x == o.x) || (Double.isNaN(this.x) && Double.isNaN(o.x))) {
            if (this.y == o.y) {
                return 0;
            }
            return (this.y < o.y) ? -1 : 1;
        }
        return (this.x < o.x) ? -1 : 1;
    }

    public double dist2(Point b){
        return (this.getX()-b.getX())*(this.getX()-b.getX()) + (this.getY()-b.getY())*(this.getY()-b.getY());
    }

    public boolean inSquare(double size){
        return (0<=x && x<=size && 0<=y && y<=size);
    }

    public static int minYOrderedCompareTo(Point p1, Point p2) {
        if (p1.y < p2.y) return 1;
        if (p1.y > p2.y) return -1;
        if (p1.x == p2.x) return 0;
        return (p1.x < p2.x) ? -1 : 1;
    }

    public static Point midpoint(Point p1, Point p2) {
        double x = (p1.x + p2.x) / 2;
        double y = (p1.y + p2.y) / 2;
        return new Point(x, y);
    }

    /**
     * Is a->b->c a counterclockwise turn?
     * @param a first point
     * @param b second point
     * @param c third point
     * @return { -1, 0, +1 } if a->b->c is a { clockwise, collinear; counterclocwise } turn.
     *
     * Copied directly from Point2D in Algs4 (Not taking credit for this guy)
     */
    public static int ccw(Point a, Point b, Point c) {
        double area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
        if      (area2 < 0) return -1;
        else if (area2 > 0) return +1;
        else                return  0;
    }

    public String toString() {
        return String.format("(%.3f, %.3f)", this.x, this.y);
    }

    public double distanceTo(Point that) {
        return Math.sqrt((this.x - that.x)*(this.x - that.x) + (this.y - that.y)*(this.y - that.y));
    }

    public void draw() {
    }

    public void draw(Color c) {
    }

    private static final double EPSILON = 0.0000001;

    private static boolean equals(double a, double b) {
        if (a == b)
            return true;
        return Math.abs(a - b) < EPSILON * Math.max(Math.abs(a), Math.abs(b));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Point && equals(((Point) other).x, this.x) && equals(((Point) other).y, this.y);
    }

    @Override
    public int hashCode() {
        int aux = Double.hashCode(x)+Double.hashCode(y);
        return Integer.hashCode(aux);
    }


}
