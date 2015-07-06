package geometry;

import static org.junit.Assert.*;
import geometry.scheme.DefaultTangentChooser;
import geometry.scheme.fourpoint.EndpointReflectingPointSelector;
import geometry.scheme.fourpoint.TangentCurve;

import org.junit.Before;
import org.junit.Test;

public class TestReflection {
	private Curve _curve;

	@Before
	public void setUp() {
		_curve = new TangentCurve(new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0) },
				1.0 / 16.0, 0.9, new DefaultTangentChooser());
	}

	@Test
	public void test() {
		PointSelector selector = new EndpointReflectingPointSelector();
		selector.setIndex(0);
		Point a = selector.getA(_curve);
		
		assertEquals(new Point(0, 1), a);
	}
}