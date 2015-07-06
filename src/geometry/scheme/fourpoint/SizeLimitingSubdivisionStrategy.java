package geometry.scheme.fourpoint;

import geometry.Curve;
import geometry.PointSelector;

public class SizeLimitingSubdivisionStrategy implements SubdivisionStrategy {
	private final SubdivisionStrategy _decorated;
	private final int _maxLength;

	public SizeLimitingSubdivisionStrategy(SubdivisionStrategy decorated, int maxLength) {
		_decorated = decorated;
		_maxLength = maxLength;
	}

	@Override
	public FourPointCurve subdivide(FourPointCurve currentCurve, PointSelector pointSelector, int step) {
		FourPointCurve subdivided = _decorated.subdivide(currentCurve, pointSelector, step);
		
		if (subdivided.size() > _maxLength) {
			subdivided = subdivided.createSubcurve(0, _maxLength);
		}
		
		return subdivided;
	}
}