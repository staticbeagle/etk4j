package com.wildbitsfoundry.etk4j.math.interpolation;

import java.util.Arrays;
import static com.wildbitsfoundry.etk4j.util.validation.DimensionCheckers.checkMinXLength;
import static com.wildbitsfoundry.etk4j.util.validation.DimensionCheckers.checkXYDimensions;

public class LinearSpline extends Spline {

	protected LinearSpline(double[] x, double[] y) {
		super(x, 2);

		final int n = _x.length;
		// compute coefficients
		coefficients = new double[(n - 1) * 2]; // 2 coefficients and n - 1 segments
		for (int i = 0, j = 0; i < n - 1; ++i, ++j) {
			double hx = _x[i + 1] - _x[i];
			if (hx <= 0.0) {
				throw new IllegalArgumentException("x must be monotonically increasing");
			}
			double a = (y[i + 1] - y[i]) / hx;
			double b = y[i];
			coefficients[j] = a;
			coefficients[++j] = b;
		}
	}

	public static LinearSpline newLinearSpline(double[] x, double[] y) {
		return newLinearSplineInPlace(Arrays.copyOf(x, x.length), y);
	}

	public static LinearSpline newLinearSplineInPlace(double[] x, double[] y) {
		checkXYDimensions(x, y);
		checkMinXLength(x, 2);
		return new LinearSpline(x, y);
	}

	@Override
	public double evaluateAt(int i, double x) {
		double t = x - _x[i];
		i <<= 1;
		return coefficients[i + 1] + t * coefficients[i];
	}

	@Override
	protected double evaluateDerivativeAt(int i, double x) {
		i <<= 1;
		return coefficients[i];
	}

	@Override
	public double evaluateAntiDerivativeAt(int i, double x) {
		double t = x - _x[i];
		i <<= 1;
		return t * (coefficients[i + 1] + t * coefficients[i] * 0.5);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = 0; i < _x.length - 1; ++i, ++j) {
			double a = coefficients[j];
			double b = coefficients[++j];

			sb.append("S").append(i + 1).append("(x) = ")
					.append(a != 0d ? String.format("%.4g * (x - %.4f)", a, _x[i]) : "")
					.append(b != 0d ? String.format(" + %.4g", b) : "")
					.append(System.lineSeparator());
		}
		sb.setLength(Math.max(sb.length() - System.lineSeparator().length(), 0));
		return sb.toString().replace("+ -", "- ").replace("- -", "+ ")
				.replace("=  + ", "= ").replace("=  - ", "= -");
	}

	public static void main(String[] args) {
		double[] x = { 1, 2, 3, 4 };
		double[] y = { 1, 4, 9, 16 };

		LinearSpline ls = newLinearSpline(x, y);
		CubicSpline cs = CubicSpline.newNotAKnotSpline(x, y);
		NearestNeighbor nh = NearestNeighbor.newNearestNeighbor(x, y);

		System.out.println(ls.evaluateAt(4));

		System.out.println(ls.integrate(1, 4));

		System.out.println(cs.evaluateAt(4));

		System.out.println(cs.integrate(1, 4));

		System.out.println(nh.evaluateAt(1.6));

		double[] vgs = {1.25, 1.5, 2.0, 3.5, 4.5, 5.5};
		double[] rdson = {95, 75, 47, 30, 25, 24};

		// TODO integrable function
		LinearSpline ls2 = newLinearSpline(vgs, rdson);
		System.out.println(ls2.evaluateAt(2.0));
	}
}
