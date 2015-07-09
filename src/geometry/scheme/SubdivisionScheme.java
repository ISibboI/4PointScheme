package geometry.scheme;

import geometry.Curve;

public interface SubdivisionScheme {
	Curve getResult();

	void evaluate();
}
