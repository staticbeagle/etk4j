package com.wildbitsfoundry.etk4j.math.linearalgebra;

import java.util.Arrays;
// TODO javadocs
public class SuccessiveOverRelaxation {
    private double w;
    private double[] b;
    private MatrixDense A;
    private int iterationLimit = 100;
    private double tol = 1e-9;
    private double[] x0 = null;

    public SuccessiveOverRelaxation(MatrixDense A, double[] b, double w) {
        this.A = A;
        this.b = b;
        this.w = w;
    }

    public SuccessiveOverRelaxation iterationLimit(int iterationLimit) {
        this.iterationLimit = iterationLimit;
        return this;
    }

    public SuccessiveOverRelaxation tolerance(double tolerance) {
        this.tol = tolerance;
        return this;
    }

    public SuccessiveOverRelaxation initialConditions(double[] x0) {
        this.x0 = x0;
        return this;
    }

    public IterativeSolverResults<double[]> solve() {
        double[] x = x0 == null ? new double[b.length] : x0;
        int n = b.length;
        int it = 0;
        double dxmax = Double.NaN;
        while (it++ < iterationLimit) {
            dxmax = 0;
            for (int i = 0; i < n; i++) {
                double residual = b[i];
                for (int j = 0; j < n; j++) {
                    residual -= A.unsafeGet(i, j) * x[j];
                }
                if (Math.abs(residual) > dxmax) {
                    dxmax = Math.abs(residual);
                }
                x[i] += w * residual / A.unsafeGet(i, i);
            }
            if (dxmax < tol) {
                IterativeSolverResults<double[]> solverResults = new IterativeSolverResults<>();
                solverResults.setSolverStatus("Converged");
                solverResults.setHasConverged(true);
                solverResults.setError(dxmax);
                solverResults.setValue(x);
                solverResults.setNumberOfIterations(it);
                return solverResults;
            }
        }
        IterativeSolverResults<double[]> solverResults = new IterativeSolverResults<>();
        solverResults.setSolverStatus("Maximum number of iterations exceeded");
        solverResults.setHasConverged(false);
        solverResults.setError(dxmax);
        solverResults.setValue(x);
        solverResults.setNumberOfIterations(it);
        return solverResults;
    }

    public static void main(String[] args) {
        double[][] A = {{4, -1, 0, 1, 0},
                {-1, 4, -1, 0, 1},
                {0, -1, 4, -1, 0},
                {1, 0, -1, 4, -1},
                {0, 1, 0, -1, 4}};

        double[] b = {100, 100, 100, 100, 100};

        double[] x = new SuccessiveOverRelaxation(new MatrixDense(A), b, 1)
                .iterationLimit(20)
                .tolerance(0.000001)
                .solve().getValue();
        System.out.println(Arrays.toString(x));
    }
}
