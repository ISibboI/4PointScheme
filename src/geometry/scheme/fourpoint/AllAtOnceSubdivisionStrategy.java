package geometry.scheme.fourpoint;

import geometry.PointSelector;

public class AllAtOnceSubdivisionStrategy implements SubdivisionStrategy {
	@Override
	public FourPointCurve subdivide(FourPointCurve currentCurve, PointSelector pointSelector, int step) {
		return currentCurve.subdivide(pointSelector, step);
	}
}