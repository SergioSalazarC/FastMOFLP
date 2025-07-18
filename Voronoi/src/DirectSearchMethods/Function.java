package DirectSearchMethods;

public abstract class Function {
	
	protected int dimension; // #Variables (size of the solution array)
	private String name;
	private double lowerBound;
	private double upperBound;
	private int numEvaluations = 0;
	
	public Function(String name, int dimension, double lowerBound, double upperBound) {
		this.name = name;
		this.dimension = dimension;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public double evaluate(double[] solution) {
		
		numEvaluations++;
		
		return internalEvaluate(solution);
		
	}
	
	protected abstract double internalEvaluate(double[] solution);

	public String getName() {
		return name;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public double getUpperBound() {
		return upperBound;
	}
	
	public double getLowerBound() {
		return lowerBound;
	}
	
	public double getBoundsWidth() {
		return this.upperBound - this.lowerBound;
	}

	public int getNumEvaluations() {
		return numEvaluations;
	}
}
