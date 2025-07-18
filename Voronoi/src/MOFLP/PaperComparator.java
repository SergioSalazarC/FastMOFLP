package MOFLP;

import java.util.HashSet;

public class PaperComparator {

    static double[] n100rp= new double[21];
    static double[] n100r2p= new double[21];
    static double[] n1000rp= new double[21];
    static double[] n1000r2p= new double[21];

    static double EPS = 1E-6;

    public PaperComparator() {
        //n=100 d=1/r(2p)
        n100r2p[2] = 0.154282;
        n100r2p[3] = 0.151738;
        n100r2p[4] = 0.150887;
        n100r2p[5] = 0.119135;
        n100r2p[6] = 0.111488;
        n100r2p[7] = 0.110668;
        n100r2p[8] = 0.108818;
        n100r2p[9] = 0.106636;
        n100r2p[10] = 0.102188;
        n100r2p[11] = 0.101100;
        n100r2p[12] = 0.101100;
        n100r2p[13] = 0.101100;
        n100r2p[14] = 0.100538;
        n100r2p[15] = 0.100538;
        n100r2p[16] = 0.100538;
        n100r2p[17] = 0.100538;
        n100r2p[18] = 0.096482;
        n100r2p[19] = 0.095852;
        n100r2p[20] = 0.095394;

        //n=100 d=1/r(p)
        n100rp[2] = 0.154282;
        n100rp[3] = 0.151738;
        n100rp[4] = 0.124591;
        n100rp[5] = 0.111488;
        n100rp[6] = 0.110668;
        n100rp[7] = 0.108818;
        n100rp[8] = 0.108381;
        n100rp[9] = 0.102188;
        n100rp[10] = 0.096821;
        n100rp[11] = 0.095394;
        n100rp[12] = 0.095169;
        n100rp[13] = 0.094497;
        n100rp[14] = 0.081407;
        n100rp[15] = 0.093303;
        n100rp[16] = 0.084600;
        n100rp[17] = 0.081280;
        n100rp[18] = 0.075380;
        n100rp[19] = 0.076831;
        n100rp[20] = 0.081280;

        //n=1000 d=1/r(p)
        n1000rp[2] = 0.060413;
        n1000rp[3] = 0.048099;
        n1000rp[4] = 0.048099;
        n1000rp[5] = 0.044364;
        n1000rp[6] = 0.044364;
        n1000rp[7] = 0.043710;
        n1000rp[8] = 0.041560;
        n1000rp[9] = 0.039729;
        n1000rp[10] = 0.039729;
        n1000rp[11] = 0.038923;
        n1000rp[12] = 0.039123;
        n1000rp[13] = 0.038379;
        n1000rp[14] = 0.038075;
        n1000rp[15] = 0.038075;
        n1000rp[16] = 0.037049;
        n1000rp[17] = 0.035941;
        n1000rp[18] = 0.035011;
        n1000rp[19] = 0.035288;
        n1000rp[20] = 0.033371;

        //n=1000 d=1/r(2p)
        n1000r2p[2] = 0.060413;
        n1000r2p[3] = 0.050160;
        n1000r2p[4] = 0.048334;
        n1000r2p[5] = 0.048099;
        n1000r2p[6] = 0.048099;
        n1000r2p[7] = 0.044364;
        n1000r2p[8] = 0.044364;
        n1000r2p[9] = 0.044324;
        n1000r2p[10] = 0.043385;
        n1000r2p[11] = 0.041560;
        n1000r2p[12] = 0.041575;
        n1000r2p[13] = 0.041560;
        n1000r2p[14] = 0.041193;
        n1000r2p[15] = 0.039780;
        n1000r2p[16] = 0.039729;
        n1000r2p[17] = 0.039664;
        n1000r2p[18] = 0.039729;
        n1000r2p[19] = 0.040281;
        n1000r2p[20] = 0.040239;
    }


    public void compare(double[] resultados, boolean p, boolean cien, boolean print){
        double[] aux = n100rp;
        if(p && cien)aux = n100rp;
        if(p && !cien)aux = n1000rp;
        if(!p && cien)aux = n100r2p;
        if(!p && !cien) aux = n1000r2p;

        HashSet<Integer> iguala = new HashSet<>();
        HashSet<Integer> mejora = new HashSet<>();

        for (int i = 2; i <=20; i++) {
            if(aux[i]-EPS <= resultados[i] && resultados[i]<=aux[i]+EPS){
                iguala.add(i);
            }
            if( resultados[i]>aux[i]+EPS){
                mejora.add(i);
            }

        }
        if(!print){
            System.out.println("Iguala "+iguala.size()+" resultados y mejora "+mejora.size()+" resultados.");
        }
        else{
            for (int i = 2; i <= 20; i++) {
                System.out.print("(" + i + ")  " + resultados[i]+"    ");
                if(iguala.contains(i)) System.out.println("**");
                else if(mejora.contains(i)) System.out.println(">>");
                else System.out.println();
            }
            System.out.println();
            System.out.println("Iguala "+iguala.size()+" resultados y mejora "+mejora.size()+" resultados.");

        }


    }
}
