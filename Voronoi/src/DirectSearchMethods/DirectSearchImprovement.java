package DirectSearchMethods;


public abstract class DirectSearchImprovement {

	private static final double MAX_EVALUATIONS_EXCEEDED_VALUE = -1.0;
	private double[] previousPoint;
	private int numEvaluations;
	private double previousValue;

	public abstract int improveSolution(double[] solution, Function func, double minDistanceToConsiderSolutionForEvaluation, int maxEvaluations);

	protected double evaluate(double[] x, Function function, double minDistanceToConsiderSolutionForEvaluation, int maxEvaluations) {
		
		if(minDistanceToConsiderSolutionForEvaluation <= 0.0 || previousPoint == null) {
			if(numEvaluations == maxEvaluations) {
				return MAX_EVALUATIONS_EXCEEDED_VALUE;
			}
			double value = function.evaluate(x);
			numEvaluations++;
			return value;
		}
		
		// --------------------------------------------------------------------------
		// force to be within bounds / test if too close / too far away / too many
		// evaluations
		for (int i = 0; i < (int) function.getDimension(); i++) {
			if (x[i] < function.getLowerBound()) {
				x[i] = function.getLowerBound();
			}
			if (x[i] > function.getUpperBound()) {
				x[i] = function.getUpperBound();
			}
		}

		double dist = 0.0;
		double t;

		// too far from starting point?
		// if (max_dist_start<Math.sqrt(Double.MAX_VALUE))
		// {
		// for (int i=0;i<(int)instance.getDimension();i++)
		// {
		// t = current_start_point_.get(i)-x.get(i);
		// dist += t*t;
		// }
		//
		// if (dist>max_dist_start*max_dist_start) {
		// return false;
		// }
		//
		// }

		// too far from current point?
		// if (max_dist_current<Math.sqrt(Double.MAX_VALUE))
		// {
		// dist = 0.0;
		// for (int i=0;i<(int)instance.getDimension();i++)
		// {
		// t = cx.get(i)-x.get(i);
		// dist += t*t;
		// }
		// if (dist>max_dist_current*max_dist_current) {
		// return false;
		// }
		// }

		// too close to previous point?
		dist = 0.0;
		for (int i = 0; i < (int) function.getDimension(); i++) {
			t = previousPoint[i] - x[i];
			dist += t * t;
		}
		if (dist < minDistanceToConsiderSolutionForEvaluation * minDistanceToConsiderSolutionForEvaluation) {
			System.out.println("Non evaluated");
			previousPoint = x.clone();
			return previousValue;
		}
		
		// ok to evaluate point:
		if(numEvaluations == maxEvaluations) {
			return MAX_EVALUATIONS_EXCEEDED_VALUE;
		}
		double value = function.evaluate(x);
		numEvaluations++;
		previousPoint = x.clone();
		previousValue = value;
		return value;

	}
	
	/**
	 * <p>This method copies, element by element, <i>solutionToCopy</i> into <i>solution</i>. As a result, after
	 * this method has been executed,</p> 
	 * 
	 * <code>
	 * Arrays.deepEquals(solution, solutionToCopy) == true.
	 * </code>
	 * 
	 * @param solution The solution that will be modified to make it equals to <i>solutionToCopy</i>
	 * @param solutionToCopy The solution that is going to be copied
	 */
	protected void copySolution(double[] solution, double[] solutionToCopy) {
		for(int i = 0; i < solution.length; i++) {
			solution[i] = solutionToCopy[i];
		}
	}
	
	protected int getNumEvaluations() {
		return numEvaluations;
	}
	
	protected void resetEvaluations() {
		numEvaluations = 0;
	}

}
