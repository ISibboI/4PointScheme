package geometry.scheme;

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
	public Curve subdivide(Curve currentCurve, PointSelector pointSelector, int step) {
		Curve subdivided = _decorated.subdivide(currentCurve, pointSelector, step);
		
		if (subdivided.size() > _maxLength) {
			subdivided = subdivided.createSubcurve(0, _maxLength);
		}
		
		return subdivided;
	}
}