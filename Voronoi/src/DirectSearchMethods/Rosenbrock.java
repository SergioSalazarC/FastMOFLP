package DirectSearchMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * No es igual que la implementación original. La condición de parada
 * de la implementación original no existe. 
 * 
 * @author Francisco Gortázar <patxi.gortazar@gmail.com>
 *
 */
public class Rosenbrock extends DirectSearchImprovement {

	private double delta;

	public Rosenbrock() {
		this(1.5);
	}
	
	public Rosenbrock(double delta) {
		this.delta = delta;
	}

	@Override
	public int improveSolution(double[] x, Function func, double minDistanceToConsiderSolutionForEvaluation,
			int maxEvaluations) {
		resetEvaluations();
		
		  int n = func.getDimension();

		  //cout << "Global best: " << *fbest << "\tstart: " << *f << endl;

		  // turn solution into vector (so that I can use my previous code)
		  double[] cx = new double[n]; // current x
		  double[] nx = new double[n]; // new x
		  double[] tx; // temporary x
		  double cf; // current f(x)
		  double nf = Double.POSITIVE_INFINITY; // new f(x)
		  for (int i=0;i<n;i++)
		  {
		    cx[i] = x[i];
		  }
		  cf = func.evaluate(x);

		  // create initial directions (carthesian unit vectors):
		  List<double[]> ksi = new ArrayList<double[]>(n);
		  for (int i=0;i<n;i++)
		  {
		    ksi.add(new double[n]);
		    ksi.get(i)[i] = 1.0;
		  }
		  // initialize step length:
		  double[] e = new double[n];
		  for (int i=0;i<n;i++)
		  {
		    e[i] = delta;
		  }

		  double precision = 1e-010;

		  double alpha = 3.0; // >1
		  double beta = 0.5; // 0< && <1
		  double global_best = cf;

		  boolean done = false;
		  while (!done)
		  {
		    double[] d = new double[n];

		    double value = cf;
		    // for each direction:
		    for (int j=0;j<n && !done;j++)
		    {
		      // search in order to find at least one success (improvement) and one failure
		      int failure=0, success=0;
		      while ((failure<1 || success<1) && !done)
		      {
		        nx = new double[n];
		        double realdelta = 0.0;
		        for (int i=0;i<n;i++)
		        {
		          nx[i] = cx[i] + e[j]*ksi.get(j)[i];
		          realdelta += e[j]*ksi.get(j)[i]*e[j]*ksi.get(j)[i];
		        }
		        realdelta = Math.sqrt(realdelta);
		        nf = evaluate(nx, func, minDistanceToConsiderSolutionForEvaluation, maxEvaluations);
		        if (nf < 0.0) {
		          done = true;
		          break;
		        }
		        if (nf<cf)
		        {
		          // update current point:
		          cf = nf;
		          tx = cx;
		          cx = nx;
		          nx = tx;
		          success++;
		          // keep track of how much movement has been made in each direction
		          d[j] += e[j];
		          e[j] *= alpha;
		          // global best?
		          if (cf<global_best)
		          {
		            global_best = cf;
		          }
		        } else {
		          // failure:
		          failure++;
		          e[j] *= -beta;
		        }
		        // test for termination:
		        //if (functions[index].tempOpt)
		        //  done = true;
		        // special one to avoid "infinite loops" in the Rosenbrock:
		        if (realdelta<=minDistanceToConsiderSolutionForEvaluation*delta)
		        {
		          //cout << "Shouldn't be evaluated if holding the epsiolon*delta restriction: " << realdelta << endl;
		          success = failure = 1; // just to get out of the loop
		        }

		        //cout << "\tnot best : " << global_best << "\tnew maxevals : " << evals_max << "\tevals : " << evals << "\tvalues: " << (value-cf) << endl;


		        // special one to avoid "infinite loops" in the Rosenbrock:
		        //if (realdelta<=0.0)
		        /*if (realdelta<=epsilon*delta)
		        {
		          cout << "Shouldn't be evaluated if holding the epsiolon*delta restriction: " << realdelta << endl;
		        }
		        if (realdelta<=0.0001)
		        {
		          cout << "realdelta: " << realdelta << " for j = " << j << " after evals = " << evals << "(" << *evalcount << ")" << endl;
		        }
		        if (realdelta<=precision)
		        {
		          success = failure = 1; // TODO: reconsider this?
		          cout << "realdelta: " << realdelta << " for j = " << j << endl;
		        }*/
		        if (e[j]==0.0) 
		        {
		          e[j] = delta;
		          break;
		        }
		      }
		    }
		    // also special in this implementation (to avoid recalculating A when no moves have been made:
		    if (value-cf<=0.0)
		    {
		      done = true;
		      //cout << "Break due to no improvement" << endl;
		    }

		    if (!done)
		    {
		      // after looping through all directions: find new directions!
		      // using d, ksi, A. 
		      // norms of A stored in t.
		      List<double[]> A = new ArrayList<double[]>(n);
		      double[] Anorm = new double[n];
		      // find A:
		      for (int k=0;k<n;k++)
		      {
		        A.add(new double[n]);
		        for (int j=k;j<n;j++)
		        {
		          for (int i=0;i<n;i++)
		          {
		            A.get(k)[i] += d[j]*ksi.get(j)[i];
		          }
		        }
		        Anorm[k] = Math.sqrt(inner_product(A.get(k), A.get(k)));
		      }
		      // following Palmer to create next directions (using (5) and (11)):
		      for (int i=0;i<n;i++)
		      {
		        ksi.get(0)[i] = A.get(0)[i]/Anorm[0];
		      }
		      for (int k=1;k<n;k++)
		      {
		        double div = Anorm[k-1]*Anorm[k]*Math.sqrt(Anorm[k-1]*Anorm[k-1]-Anorm[k]*Anorm[k]);
		        if (div>0)
		        {
		          for (int i=0;i<n;i++)
		          {
		            ksi.get(k)[i] = ( A.get(k)[i]*Anorm[k-1]*Anorm[k-1] - A.get(k-1)[i]*Anorm[k]*Anorm[k] )/div;
		          }
		        }
		      }
		      // set new step-lengths:
		      double eavg = 0.0;
		      for (int i=0;i<n;i++) {
		        eavg += Math.abs(e[i]);
		      }
		      Arrays.fill(e, eavg/n);
		    }
		  }

		  // save best solution in "*solution":
		  for (int i=0;i<n;i++)
		  {
		    x[i] = cx[i];
		  }

		return getNumEvaluations();
	}

	private double inner_product(double[] a, double[] b) {
		  double res = 0;
		  assert(a.length==b.length);
		  for (int i=0;i<a.length;i++)
		  {
		    res += a[i]*b[i];
		  }
		  return res;
	}

}
