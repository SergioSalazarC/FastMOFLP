package DirectSearchMethods;

public 	class PointMDS implements Comparable<PointMDS> {
	double[] point;
	double value;
	
	public PointMDS(int d) {
		point = new double[d];
	}

	public void set(int index, double value) {
		point[index] = value;
	}

	public double get(int j) {
		return point[j];
	}

	public void setValue(double value) {
		this.value = value;			
	}

	public double getValue() {
		return value;
	}

	@Override
	public int compareTo(PointMDS aPointMDS) {
		if(value < aPointMDS.value) {
			return -1;
		} 
		if(value > aPointMDS.value) {
			return 1;
		}
		for(int i = 0; i < point.length; i++) {
			if(point[i] < aPointMDS.point[i]) {
				return -1;
			}
			if(point[i] > aPointMDS.point[i]) {
				return 1;
			}
		}
		return 0;
	}

	public double[] getDoubleArray() {
		return point;
	}

}


