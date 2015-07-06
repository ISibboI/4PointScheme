package geometry.scheme;

import geometry.Curve;
import geometry.PointSelector;

public class LongestFirstSubdivisionStrategy implements SubdivisionStrategy {
	@Override
	public Curve subdivide(Curve curve, PointSelector pointSelector, int step) {
//		int longestEdgeIndex = (int) (Math.random() * curve.size() - 1);
//		double longestEdge = curve.getPoint(longestEdgeIndex).squaredDistanceTo(curve.getPoint(longestEdgeIndex + 1));
		int longestEdgeIndex = -1;
		double longestEdge = 0;

		for (int i = 0; i < curve.size() - 1; i++) {
			double length = curve.getPoint(i).squaredDistanceTo(curve.getPoint(i + 1));

			if (length > longestEdge) {
				longestEdgeIndex = i;
				longestEdge = length;
			}
		}

		if (longestEdgeIndex == -1) {
			throw new RuntimeException("Cannot find a longest edge!");
		}

		return curve.subdivide(pointSelector, step, longestEdgeIndex);
	}
}