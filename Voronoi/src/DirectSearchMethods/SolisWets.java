package DirectSearchMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SolisWets extends DirectSearchImprovement {

	private static int phase;
	private static double V1;
	private static double V2;
	private static double S;

	private double initDelta;
	private double minDelta;

	public SolisWets(double initDelta, double minDelta) {
		this.initDelta = initDelta;
		this.minDelta = minDelta;
	}

	public SolisWets() {
		this(1.45, 0.06);
	}

	@Override
	public int improveSolution(double[] solution, Function func, double minDistanceToEvaluateSolution, int maxEvaluations) {

		resetEvaluations();

		double delta = initDelta * func.getBoundsWidth();
		double delta_min = minDelta * delta;

		double cf; // current f(x)
		double nf = 0.0; // new f(x)

		double[] cx = solution.clone();
		double originalValue = func.evaluate(solution);
		cf = originalValue;

		double[] nx = new double[func.getDimension()];

		// perform SW:

		int n_succ = 0, n_fail = 0;
		List<Double> bias = new ArrayList<Double>();
		for (int i = 0; i < func.getDimension(); i++) {
			bias.add(0.0);
		}

		double rho = delta / Math.sqrt((double) func.getDimension());
		double rhomin = delta_min / Math.sqrt((double) func.getDimension());

		double global_best = originalValue;

		boolean done = false;
		while (!done) {
			// generate dx
			List<Double> dx = new ArrayList<Double>();
			for (int i = 0; i < func.getDimension(); i++) {
				dx.add(rho * gaussrand());
			}
			for (int i = 0; i < func.getDimension(); i++) {
				nx[i] = cx[i] + dx.get(i) + bias.get(i);
			}
			double value = evaluate(nx, func, minDistanceToEvaluateSolution, maxEvaluations);
			if (value < 0.0) {
				done = true;
				break;
			}
			nf = value;
			if (nf < cf) {
				double[] tx = cx;
				cx = nx;
				nx = tx;
				cf = nf;
				if (cf < global_best) {
					global_best = cf;
				}
				for (int i = 0; i < func.getDimension(); i++) {
					bias.set(i, 0.2 * bias.get(i) + 0.4 * (dx.get(i) + bias.get(i)));
				}
				n_succ++;
				n_fail = 0;
			} else {
				for (int i = 0; i < func.getDimension(); i++) {
					nx[i] = cx[i] - dx.get(i) - bias.get(i);
				}
				value = evaluate(nx, func, minDistanceToEvaluateSolution, maxEvaluations);
				if (value < 0.0) {
					done = true;
					break;
				}
				nf = value;
				if (nf < cf) {
					double[] tx = cx;
					cx = nx;
					nx = tx;
					cf = nf;
					if (cf < global_best) {
						global_best = cf;
					}
					for (int i = 0; i < func.getDimension(); i++) {
						bias.set(i, bias.get(i) - 0.4 * (dx.get(i) + bias.get(i)));
					}
					n_succ++;
					n_fail = 0;
				} else {
					// both failed:
					for (int i = 0; i < func.getDimension(); i++) {
						bias.set(i, bias.get(i) / 2.0);
					}
					n_fail++;
					n_succ = 0;
				}
			}

			if (n_succ >= 5) {
				n_succ = 0;
				rho /= 0.5; // same settings as in Hvattum, Glover. 2008.
			}
			if (n_fail >= 3) {
				n_fail = 0;
				rho *= 0.5;
			}

			// termination?
			if (rho < rhomin) {
				done = true;
			}
			// if (functions[index].tempOpt)
			// done = true;
		}

		// save best solution in "*solution":
		copySolution(solution, cx);

		return getNumEvaluations();
	}

	// References: Knuth Sec. 3.4.1 p. 117; Marsaglia and Bray,
	// "A Convenient Method for Generating Normal Variables";
	// Press et al., _Numerical Recipes in C_ Sec. 7.2 pp. 288-290.
	// in C FAQ 13.20 (via google groups)
	private double gaussrand() {
		double X;

		Random rand = new Random();

		if (phase == 0) {
			do {
				double U1 = (double) rand.nextDouble();
				double U2 = (double) rand.nextDouble();

				V1 = 2 * U1 - 1;
				V2 = 2 * U2 - 1;
				S = V1 * V1 + V2 * V2;
			} while (S >= 1 || S == 0);

			X = V1 * Math.sqrt(-2 * Math.log(S) / S);
		} else
			X = V2 * Math.sqrt(-2 * Math.log(S) / S);

		phase = 1 - phase;

		return X;
	}

}
