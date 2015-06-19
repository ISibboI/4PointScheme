package geometry.scheme;

import geometry.Curve;

public class LongestFirstSubdivisionStrategy implements SubdivisionStrategy {
	@Override
	public Curve subdivide(Curve curve, PointSelector pointSelector, int step) {
		double longestEdge = 0;
		int longestEdgeIndex = -1;

		for (int i = 0; i < curve.size() - 1; i++) {
			double length = curve.getPoint(i).squaredDistanceTo(curve.getPoint(i + 1));
			
			if (length > longestEdge) {
				longestEdgeIndex = i;
			}
		}
		
		if (longestEdgeIndex == -1) {
			throw new RuntimeException("Cannot find a longest edge!");
		}
		
		return curve.subdivide(pointSelector, step, longestEdgeIndex);
	}
}