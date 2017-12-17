package com.wildbitsfoundry.etk4j.systems.filters;

import com.wildbitsfoundry.etk4j.math.polynomials.Polynomial;
import com.wildbitsfoundry.etk4j.math.polynomials.RationalFunction;
import com.wildbitsfoundry.etk4j.systems.TransferFunction;
import com.wildbitsfoundry.etk4j.systems.filters.FilterSpecs.BandPassSpecs;
import com.wildbitsfoundry.etk4j.systems.filters.FilterSpecs.BandStopSpecs;
import com.wildbitsfoundry.etk4j.systems.filters.FilterSpecs.HighPassSpecs;
import com.wildbitsfoundry.etk4j.systems.filters.FilterSpecs.LowPassSpecs;

public class AnalogFilter {
	
	int _order;
	TransferFunction _tf;
	
	static class LowPassPrototype {
		private double _eps;
		private TransferFunction _tf;
		
		public LowPassPrototype(double eps, TransferFunction tf) {
			_eps = eps;
			_tf = tf;
		}
	}
	
	protected AnalogFilter(int order, TransferFunction tf) {
		_order = order;
		_tf = tf;
	}
	
	/***
	 * Calculate the minimum order required for Low-Pass Chebyshev filter
	 * 
	 * @param fp
	 *            passband frequency in Hertz
	 * @param fs
	 *            stopband frequency in Hertz
	 * @param ap
	 *            passband attenuation in dB
	 * @param as
	 *            stopband attenuation in dB
	 * @return
	 */
	public static int getMinOrderNeeded(double fp, double fs, double ap, double as, ApproximationType type) {
		return type.getMinOrderNeeded(fp, fs, ap, as);
	}
	
	public static AnalogFilter newLowPassFilter(int n, double ap, ApproximationType type) {
		LowPassPrototype lp = type.buildLowPassPrototype(n, ap);
		return new AnalogFilter(n, lp._tf);
	}
	
	public static AnalogFilter newLowPassFilter(LowPassSpecs specs) {
		double fp = specs.PassBandFrequency;
		double fs = specs.StopBandFrequency;
		double ap = specs.PassBandAttenuation;
		double as = specs.StopBandAttenuation;
		
		double wp = 2 * Math.PI * fp;
		double ws = 2 * Math.PI * fs;
		final int n = specs.ApproximationType.getMinOrderNeeded(wp, ws, ap, as);
		LowPassPrototype lp = specs.ApproximationType.buildLowPassPrototype(n, ap);
		double factor = Math.pow(lp._eps, -1.0 / n) * wp;
		lp._tf.substituteInPlace(1.0 / factor);
		return new AnalogFilter(n, lp._tf);
	}
	
	public static AnalogFilter newHighPass(int n, double ap, ApproximationType type) {
		LowPassPrototype lp = type.buildLowPassPrototype(n, ap);
		lp._tf = lpTohp(lp._tf.getNumerator(), lp._tf.getDenominator());
		return new AnalogFilter(n, lp._tf);
	}
	
	public static AnalogFilter newHighPassFilter(HighPassSpecs specs) {
		double fp = specs.PassBandFrequency;
		double fs = specs.StopBandFrequency;
		double ap = specs.PassBandAttenuation;
		double as = specs.StopBandAttenuation;
		
		double wp = 2 * Math.PI * fp;
		double ws = 2 * Math.PI * fs;
		final int n = specs.ApproximationType.getMinOrderNeeded(wp, ws, ap, as);
		LowPassPrototype lp = specs.ApproximationType.buildLowPassPrototype(n, ap);
		double factor = wp / Math.pow(lp._eps, -1.0 / n);
		lp._tf.substituteInPlace(factor);
		lp._tf = lpTohp(lp._tf.getNumerator(), lp._tf.getDenominator());
		return new AnalogFilter(n, lp._tf);
	}
	
	public static AnalogFilter newBandPassFilter(BandPassSpecs specs) {
		double fp1 = specs.LowerPassBandFrequency;
		double fp2 = specs.UpperPassBandFrequency;
		double fs1 = specs.LowerStopBandFrequency;
		double fs2 = specs.UpperStopBandFrequency;
		double ap = specs.PassBandAttenuation;
		double as1 = specs.LowerStopBandAttenuation;
		double as2 = specs.UpperStopBandAttenuation;
		
		double wp1 = 2 * Math.PI * fp1;
		double wp2 = 2 * Math.PI * fp2;
		double ws1 = 2 * Math.PI * fs1;
		double ws2 = 2 * Math.PI * fs2;

		double w0 = Math.sqrt(wp1 * wp2);
		double Q = w0 / (wp2 - wp1);

		double whs1 = ws1 / w0;
		double whs2 = ws2 / w0;

		double omega1 = Q * Math.abs((whs1 * whs1 - 1) / whs1);
		double omega2 = Q * Math.abs((whs2 * whs2 - 1) / whs2);
		

		final int n1 = specs.ApproximationType.getMinOrderNeeded(1, omega1, ap, as1);
		final int n2 = specs.ApproximationType.getMinOrderNeeded(1, omega2, ap, as2);
		
		int n = n1;
		double omega = omega1;
		if(n1 < n2) {
			n = n2;
			omega = omega2;
		}
		double bpa = specs.ApproximationType.getBandPassAp(ap, as1, as2);
		LowPassPrototype lp = specs.ApproximationType.buildLowPassPrototype(n, bpa);

		double bw = specs.ApproximationType.getBandPassBW(n, lp._eps, Q, w0, omega);
		lp._tf = lpTobp(lp._tf.getNumerator(), lp._tf.getDenominator(), w0, bw);
		n <<= 1;
		return new AnalogFilter(n, lp._tf);
	}
	
	public static AnalogFilter newBandStopFilter(BandStopSpecs specs) {
		double fp1 = specs.LowerPassBandFrequency;
		double fp2 = specs.UpperPassBandFrequency;
		double fs1 = specs.LowerStopBandFrequency;
		double fs2 = specs.UpperStopBandFrequency;
		double amax = specs.PassBandAttenuation;
		double amin = specs.StopBandAttenuation;
		
		double wp1 = 2 * Math.PI * fp1;
		double wp2 = 2 * Math.PI * fp2;
		double ws1 = 2 * Math.PI * fs1;
		double ws2 = 2 * Math.PI * fs2;
		double w0 = Math.sqrt(wp1 * wp2);
		double Q = w0 / (wp2 - wp1);

		double whs1 = ws1 / w0;
		double whs2 = ws2 / w0;

		double omegas1 = 1 / (Q * Math.abs((whs1 * whs1 - 1) / whs1));
		double omegas2 = 1 / (Q * Math.abs((whs2 * whs2 - 1) / whs2));
		
		final int n1 = specs.ApproximationType.getMinOrderNeeded(1, omegas1, amax, amin);
		final int n2 = specs.ApproximationType.getMinOrderNeeded(1, omegas2, amax, amin);
		
		int n = n1;
		double omegas = omegas1;
		if(n1 < n2) {
			n = n2;
			omegas = omegas2;
		}
		
		double bsa = specs.ApproximationType.getBandStopAp(amax, amin);
		LowPassPrototype lp = specs.ApproximationType.buildLowPassPrototype(n, bsa);

		double bw = specs.ApproximationType.getBandStopBW(n, lp._eps, Q, w0, omegas);
		lp._tf = lpTobp(lp._tf.getNumerator(), lp._tf.getDenominator(), w0, bw);
		n <<= 1;
		return new AnalogFilter(n, lp._tf);
	}
	
	public int getOrder() {
		return _order;
	}

	public static TransferFunction lpTobp(Polynomial num, Polynomial den, double w0, double bw) {
		Polynomial s = new Polynomial(1, 0);
		Polynomial s2w02 = new Polynomial(bw, 0, bw * w0 * w0);

		RationalFunction bp = new RationalFunction(num, den);
		bp.substituteInPlace(new RationalFunction(s2w02, s));

		bp.normalize();

		return new TransferFunction(bp);
	}

	public static TransferFunction lpTobs(Polynomial num, Polynomial den, double w0, double bw) {
		Polynomial s = new Polynomial(1, 0);
		Polynomial s2w02 = new Polynomial(bw, 0, bw * w0 * w0);

		RationalFunction bp = new RationalFunction(num, den);
		bp.substituteInPlace(new RationalFunction(s, s2w02));

		bp.normalize();

		return new TransferFunction(bp);
	}

	public static TransferFunction lpTohp(Polynomial numerator, Polynomial denominator) {
		return lpTohp(numerator.getCoefficients(), denominator.getCoefficients());
	}

	public static TransferFunction lpTohp(double[] numerator, double[] denominator) {
		final int numDegree = numerator.length - 1;
		final int denDegree = denominator.length - 1;
		final int filterOrder = numDegree + denDegree + 1;

		// Reverse coefficients then scale them by the
		// order of the denominator i.e. pad with zeros
		double[] hpNumerator = new double[filterOrder];
		for (int i = numDegree, j = 0; i >= 0; --i, ++j) {
			hpNumerator[j] = numerator[i];
		}

		// Reverse coefficients then scale them by the
		// order of the numerator i.e. pad with zeros
		double[] hpDenominator = new double[filterOrder];
		for (int i = denDegree, j = 0; i >= 0; --i, ++j) {
			hpDenominator[j] = denominator[i];
		}

		return new TransferFunction(hpNumerator, hpDenominator);
	}
}
