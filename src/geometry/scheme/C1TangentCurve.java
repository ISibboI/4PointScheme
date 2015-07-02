package geometry.scheme;

import geometry.Line;
import geometry.Point;
import geometry.PointSelector;

public class C1TangentCurve extends TangentCurve {
	public C1TangentCurve(final int size, final double tensionParameter, final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(size, tensionParameter, displacementParameter, tangentChooser);
	}

	public C1TangentCurve(final Point[] points, final double tensionParameter, final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(points, tensionParameter, displacementParameter, tangentChooser);
	}

	public C1TangentCurve(final TangentCurve tangentCurve) {
		super(tangentCurve);
	}

	public C1TangentCurve(final DefaultCurve defaultCurve, final double displacementParameter,
			final TangentChooser tangentChooser) {
		super(defaultCurve, displacementParameter, tangentChooser);
	}

	@Override
	protected Point getNewPoint(final PointSelector selector) {
		Point b = selector.getB(this);
		Point c = selector.getC(this);
		Line tb = getTangent(selector.getIndex());
		Line tc = getTangent(selector.getIndex() + 1);
		
		Point bc = c.sub(b);
		Point cb = b.sub(c);
		
		Point bs = tb.getDirection();
		
		if (bs.scalarProduct(bc) < 0) {
			bs = bs.mul(-1);
		}
		
		Point cs = tc.getDirection();
		
		if (cs.scalarProduct(cb) < 0) {
			cs = cs.mul(-1);
		}
		
		bc = bc.normalize();
		cb = cb.normalize();
		bs = bs.normalize().mul(getDisplacementParameter());
		cs = cs.normalize().mul(getDisplacementParameter());
		
		Line lb = new Line(b, b.add(bs).add(bc));
		Line lc = new Line(c, c.add(cs).add(cb));
		
		double cut = lb.cut(lc);
		return lb.getStart().add(lb.getDirection().mul(cut));
	}

	@Override
	public TangentCurve clone() {
		return new C1TangentCurve(this);
	}
	
	@Override
	protected TangentCurve doubleSize() {
		return new C1TangentCurve(size() * 2 - 1, getTensionParameter(), getDisplacementParameter(), getTangentChooser());
	}
	
	@Override
	protected TangentCurve incrementSize() {
		return new C1TangentCurve(size() + 1, getTensionParameter(), getDisplacementParameter(), getTangentChooser());
	}
}
