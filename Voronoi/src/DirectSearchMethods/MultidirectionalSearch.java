package DirectSearchMethods;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Sequential Multidirectional Search Method.
 * 
 * @author Francisco Gortázar <patxi.gortazar@gmail.com>
 * 
 */
public class MultidirectionalSearch extends DirectSearchImprovement {

	private double delta;
	private double deltaMin;

	public MultidirectionalSearch() {
		this(0.22, 0.01);
	}

	public MultidirectionalSearch(double delta, double deltaMin) {
		this.delta = delta;
		this.deltaMin = deltaMin;
	}

	@Override
	public int improveSolution(double[] x, Function func, double minDistanceToConsiderSolutionForEvaluation,
							   int maxEvaluations) {

		resetEvaluations();

		int n = func.getDimension();

		PointMDS cx_p = new PointMDS(n);
		// Point * nx_p = new Point();
		// Point * tx_p;
		// double cf; // current f(x)
		for (int i = 0; i < n; i++) {
			cx_p.set(i, x[i]);
		}
		// cf = *f;
		double cx_p_f = func.evaluate(x);
		cx_p.setValue(cx_p_f);

		double global_best = cx_p_f;
		boolean done = false;

		double mds_contr = 0.5;
		double mds_exp = 2.0;

		// misc. data:
		List<PointMDS> simplex = new ArrayList<PointMDS>(); // storing simplex

		// create initial simplex:
		simplex.add(cx_p);
		for (int i = 0; i < n; i++) {
			PointMDS nx_p = new PointMDS(n);
			for (int j = 0; j < n; j++) {
				nx_p.set(j, cx_p.get(j) + (j == i ? delta : 0.0));
			}
			nx_p.value = evaluate(nx_p.point, func, minDistanceToConsiderSolutionForEvaluation, maxEvaluations);
			if (nx_p.value < 0.0) {
				done = true;
				break;
			}
			simplex.add(nx_p);
			if (nx_p.getValue() < global_best) {
				global_best = nx_p.getValue();
			}
		}

		Collections.sort(simplex);

		while (!done) {
			// do one iteration:

			// reflect
			List<PointMDS> vr = new ArrayList<PointMDS>();
			vr.add(simplex.get(0));
			for (int j = 1; j < (int) simplex.size(); j++) {
				PointMDS vrj = new PointMDS(n);
				for (int i = 0; i < n; i++) {
					vrj.set(i, 2 * simplex.get(0).get(i) - simplex.get(j).get(i));
				}
				// vrj->evaluate(xrj, f);
				vrj.value = evaluate(vrj.point, func, minDistanceToConsiderSolutionForEvaluation,
						maxEvaluations);
				if (vrj.value < 0.0) {
					done = true;
					break;
				} else {
					vr.add(vrj);
				}
				if (vrj.getValue() < global_best) {
					global_best = vrj.getValue();
				}
			}

			Collections.sort(vr);

			if (!done && vr.get(0).compareTo(simplex.get(0)) < 0 && vr.size() == simplex.size()) {
				// reflection is better than previous simplex: expand
				List<PointMDS> ve = new ArrayList<PointMDS>();
				ve.add(simplex.get(0));
				for (int j = 1; j < (int) simplex.size(); j++) {
					PointMDS vej = new PointMDS(n);
					for (int i = 0; i < n; i++) {
						vej.set(i, simplex.get(0).get(i) + mds_exp * (simplex.get(0).get(i) - simplex.get(j).get(i)));
					}
					// all_points_.push_back(vej);
					vej.value = evaluate(vej.point, func, minDistanceToConsiderSolutionForEvaluation,
							maxEvaluations);
					if (vej.value < 0.0) {
						done = true;
						break;
					} else {
						ve.add(vej);
					}
					if (vej.getValue() < global_best) {
						global_best = vej.getValue();
					}
				}
				if (done)
					break;

				Collections.sort(ve);
				if (ve.get(0).compareTo(vr.get(0)) < 0 && ve.size() == simplex.size()) {
					simplex = ve;
				} else {
					simplex = vr;
				}
			} else {
				// previous simplex is better than reflexion: contract
				delta *= 0.5;
				List<PointMDS> vc = new ArrayList<PointMDS>();
				vc.add(simplex.get(0));
				for (int j = 1; j < (int) simplex.size(); j++) {
					PointMDS vcj = new PointMDS(n);
					for (int i = 0; i < n; i++) {
						vcj.set(i, simplex.get(0).get(i) - mds_contr * (simplex.get(0).get(i) - simplex.get(j).get(i)));
					}
					vcj.value = evaluate(vcj.point, func, minDistanceToConsiderSolutionForEvaluation,
							maxEvaluations);
					if (vcj.value < 0.0) {
						done = true;
						break;
					} else {
						vc.add(vcj);
					}
					if (vcj.getValue() < global_best) {
						global_best = vcj.getValue();
					}
				}
				Collections.sort(vc);
				simplex = vc;
			}

			// test for termination:
			// if (functions[index].tempOpt)
			// done = true;
			if (delta < deltaMin)
				done = true;
		}

		Collections.sort(simplex);
		cx_p = simplex.get(0);

		// save best solution in "*solution":
		copySolution(x, cx_p.point);

		return getNumEvaluations();
	}

}
