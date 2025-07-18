package DirectSearchMethods;

import java.util.Random;


public class SimplexSearch extends DirectSearchImprovement {

	private int nfunk;
	private double alpha = 7.0;
	private double beta = 0.5;
	private double gamma = 2.0;
	private long randomSeed = 1234;

	@Override
	public int improveSolution(double[] solution, Function func, double minDistanceToEvaluateSolution, int maxEvaluations) {
		double p[][], y[], perturb, xold;
		
		resetEvaluations();
		
		Random random = new Random(randomSeed);
		
		double originalValue = func.evaluate(solution); 
		double value = originalValue;
		
		double[] original = solution.clone();
		
		int numVar = func.getDimension();
		
		p = new double[numVar + 2][numVar + 1];
		y = new double[numVar + 2];

		//First point of the simplex. Solution being improved
		for(int i = 1; i <= numVar; i++){
			p[1][i] = solution[i - 1];
		}

		y[1] = originalValue;

		for(int i = 2; i <= numVar + 1; i++){
			for(int j = 1; j <= numVar; j++){
				p[i][j] = solution[j - 1];
			}
		}

		boolean done = false;
		for(int j = 1; j <= numVar; j++){
			xold        = solution[j - 1];
			double boundsFactor = 0.01 * (func.getUpperBound()-func.getLowerBound());
			perturb     = -1.0 + 2.0*(random.nextDouble());
			perturb *= boundsFactor;
			double varValue = makeMovement(xold, perturb, func.getLowerBound(), func.getUpperBound());
			if(varValue < func.getLowerBound()) {
				varValue = func.getLowerBound();
			} else if(varValue > func.getUpperBound()) {
				varValue = func.getUpperBound();
			}
			solution[j - 1] = varValue;
			
			p[j + 1][j] = solution[j - 1];
			y[j+1] = evaluate(solution, func, minDistanceToEvaluateSolution, maxEvaluations);
			if(y[j+1] < 0.0) {
				done = true;
				break;
			} 
			solution[j-1] = xold;
		}

		if(!done) {

			/* Call Nelder and Mead's Simplex method */
			amoeba(p, y, numVar, 0.001, func, minDistanceToEvaluateSolution, maxEvaluations);

			for(int i = 1; i <= numVar + 1; i++){
				if(value > y[i]){
					for(int j = 1; j <= numVar; j++){
						solution[j - 1] = p[i][j];
						if(solution[j - 1] > func.getUpperBound()){
							solution[j - 1] = func.getUpperBound();
						}
						if(solution[j - 1] < func.getLowerBound()){
							solution[j - 1] = func.getLowerBound();
						}
					}
					value = y[i];
				}
			}

		}
		
		double solValue = func.evaluate(solution);
		if(solValue > originalValue) {
			
			//System.out.println("Solution is worst than original");
			copySolution(solution, original);
			
		}

		return getNumEvaluations();
	}
	
	private double makeMovement(double value, double step, double lowBound, double upBound){

		value += step;

		if(value > upBound){
			value = upBound - Math.abs(step)/2;
		}
		else if(value < lowBound){
			value = lowBound + Math.abs(step)/2;
		}
		return(value);
	}

	/* Nelder and Mead Simplex Method for Nonlinear unconstrained optimization problems          */
	private void amoeba(double p[][], double y[], int numVar, double ftol, Function func, double minDistanceToEvaluateSolution, int maxEvaluations){
		final int nmax = 1000;
		int i, j, ilo, ihi, inhi, mpts = numVar + 1;
		double ytry, ysave, sum, rtol, psum[];

		psum = new double[numVar + 1];
		nfunk = 0;

		for (j = 1; j <= numVar; j++){ 
			for (i = 1, sum = 0.0; i <= mpts; i++) {
				sum += p[i][j]; 
			}		
			psum[j]=sum; 
		}
		
		boolean done = false;
		while(!done){
			ilo = 1;
			if(y[1] > y[2]) {
				ihi = 1;
				inhi = 2;
			} else {
				ihi = 2;
				inhi = 1;
			}

			for (i = 1; i <= mpts; i++) {
				if (y[i] < y[ilo]){ 
					ilo = i;
				}
				if (y[i] > y[ihi]){
					inhi = ihi;
					ihi = i;
				} 
				else if (y[i] > y[inhi]){
					if (i != ihi){ 
						inhi = i;
					}
				}
			}
			
			rtol = 2.0 * Math.abs(y[ihi] - y[ilo])/(Math.abs(y[ihi]) + Math.abs(y[ilo]));
			
			if (rtol < ftol){ 
				break;
			}
			
			if (nfunk >= nmax){ 
				// No sé si hace falta reconocer que hemos salido por aquí
				return;
			}
			
			nfunk += 2;
			
			ytry = amotry(p, y, psum, numVar, ihi, -alpha, func, minDistanceToEvaluateSolution, maxEvaluations);
			
			if (ytry <= y[ilo]){
				ytry = amotry(p, y, psum, numVar, ihi, gamma, func, minDistanceToEvaluateSolution, maxEvaluations);
			}
			else if (ytry >= y[inhi]){
				ysave = y[ihi];
				ytry = amotry(p, y, psum, numVar, ihi, beta, func, minDistanceToEvaluateSolution, maxEvaluations);
				if (ytry >= ysave){
					for (i = 1; i <= mpts; i++){
						if (i != ilo) {
							for (j = 1; j <= numVar; j++){
								psum[j] = 0.5 * (p[i][j] + p[ilo][j]);
								p[i][j] = psum[j];
							}
							double[] newSol = new double[func.getDimension()];
							for(int var = 0; var < func.getDimension(); var++) {
								newSol[var] = psum[var+1];
							}
							double value = evaluate(newSol, func, minDistanceToEvaluateSolution, maxEvaluations);
							if(value < 0.0) {
								// DOn't consider this point
								y[i] = Double.POSITIVE_INFINITY;
								done = true;
								break;
							} else {
								y[i] = value;
							}
						}
					}
					nfunk += numVar;
					for (j = 1; j <= numVar; j++){ 
						for (i = 1, sum = 0.0; i <= mpts; i++) {
							sum += p[i][j]; 
						}		
						psum[j]=sum; 
					}
				}
			}
		}
	}

	double amotry(double p[][], double y[], double psum[], int ndim, int ihi, 
			double fac, Function func, double minDistanceToEvaluateSolution, int maxEvaluations) {
		
		int j;
		double fac1, fac2, ytry, ptry[];

		ptry = new double[ndim + 1];
		fac1 = (1.0 - fac)/(double)ndim;
		fac2 = fac1 - fac;
		
		for (j = 1; j <= ndim; j++){
			ptry[j] = psum[j] * fac1 - p[ihi][j] * fac2;
		}
		
		double[] newSol = new double[func.getDimension()];
		for(int var = 0; var < func.getDimension(); var++) {
			newSol[var] = ptry[var+1];
		}
		double value = evaluate(newSol, func, minDistanceToEvaluateSolution, maxEvaluations);
		if(value < 0.0) {
			ytry = Double.POSITIVE_INFINITY;
		} else {
			ytry = value;
		}
		nfunk++;
		if (ytry < y[ihi]) {
			y[ihi] = ytry;
			for (j = 1; j <= ndim; j++) {
				psum[j]  += ptry[j] - p[ihi][j];
				p[ihi][j] = ptry[j];
			}
		}
		
		return ytry;
	}

}
