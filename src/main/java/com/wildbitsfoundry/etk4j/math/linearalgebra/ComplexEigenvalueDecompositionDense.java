package com.wildbitsfoundry.etk4j.math.linearalgebra;

import com.wildbitsfoundry.etk4j.math.complex.Complex;

/**
 * Eig implements the eigenvalue-vector decomposition of
 * of a square matrix. Specifically given a diagonalizable
 * matrix A, there is a matrix nonsingular matrix X such that
 * <ul>
 * <li>D = X<sup>-1</sup> AX</li>
 * </ul>
 * is diagonal. The columns of X are eigenvectors of A corresponding
 * to the diagonal elements of D. Eig implements X as a ComplexMatrixDense and
 * D as a Zdiagmat.
 * <p>
 * Warning: if A is defective rounding error will allow Eig to
 * compute a set of eigevectors. However, the matrix X will
 * be ill conditioned.
 *
 * @author G. W. Stewart
 * @version Pre-alpha
 */
public class ComplexEigenvalueDecompositionDense {

    /**
     * The matrix of eigevectors
     */
    private ComplexMatrixDense V;

    /**
     * The diagonal matrix of eigenvalues
     */
    private ComplexMatrixDense D;

    private boolean isSymmetric;

    public ComplexEigenvalueDecompositionDense(ComplexMatrixDense A) {
        this(A, true);
    }

    /**
     * Creates an eigenvalue-vector decomposition of a square matrix A.
     *
     * @param A The matrix whose decomposition is to be
     *          computed
     *          Thrown if A is not square. <br>
     *          Passed from below.
     */
    public ComplexEigenvalueDecompositionDense(ComplexMatrixDense A, boolean balance) {

        Complex[] data = A.getArray();
        int dim = A.getColumnCount();
        isSymmetric = true;
        for (int j = 0; (j < dim) & isSymmetric; j++) {
            for (int i = 0; (i < dim) & isSymmetric; i++) {
                isSymmetric = (data[i * dim + j].equals(data[j * dim + i]));
            }
        }
        if(balance && !isSymmetric) {
            A = new ComplexMatrixDense(A.balance());
        }

        int i, j, k;
        double norm, scale;
        Complex z, d;

        if (A.getRowCount() != A.getColumnCount()) {
            throw new NonSquareMatrixException("Matrix must be squared");
        }

        int n = A.getRowCount();

        /* Compute the Schur decomposition of $A$ and set up T and D. */
        ComplexSchurDecompositionDense S = new ComplexSchurDecompositionDense(A);

        ComplexMatrixDense T = S.T;

        D = new ComplexMatrixDense(T.getRowCount(), T.getColumnCount(), new Complex());
        for(i = 0; i < T.getRowCount(); i++) {
            D.unsafeSet(i, i, T.unsafeGet(i, i));
        }

        norm = A.normFrob();

        V = new ComplexMatrixDense(n, n, new Complex());

        /* Compute the eigenvectors of T */
        for (k = n - 1; k >= 0; k--) {

            d = T.unsafeGet(k, k).copy();

            V.set(k, k, Complex.fromReal(1));
            for (i = k - 1; i >= 0; i--) {

                V.unsafeSet(i, k, T.unsafeGet(i, k).uminus());

                for (j = i + 1; j < k; j++) {

                    double a = V.unsafeGet(i, k).real();
                    double b = T.unsafeGet(i, j).real();
                    double c = V.unsafeGet(j, k).real();
                    double e = T.unsafeGet(i, j).imag();
                    double f = V.unsafeGet(j, k).imag();

                    double aa = V.unsafeGet(i, k).imag();
                    double bb = T.unsafeGet(i, j).real();
                    double cc = V.unsafeGet(j, k).imag();
                    double ee = T.unsafeGet(i, j).imag();
                    double ff = V.unsafeGet(j, k).real();
                    Complex val = new Complex(a - b * c + e * f, aa - bb * cc - ee * ff);
                    V.unsafeSet(i, k, val);
                }

                z = T.unsafeGet(i, i).copy();
                z.subtractEquals(d);
                if (z.real() == 0.0 && z.imag() == 0.0) { // perturb zero diagonal
                    z = Complex.fromReal(1.0e-16).multiply(norm);      // to avoid division by zero
                }
                z = (V.unsafeGet(i, k).divide(z));
                V.unsafeSet(i, k, z);
            }

            /* Scale the vector so its norm is one. */
            scale = 1.0 / normFro(V, 0, V.getRowCount() - 1, k, k);
            for (i = 0; i < V.getRowCount(); i++) {
                V.unsafeSet(i, k, V.unsafeGet(i, k).multiply(scale));
            }
        }
        V = S.U.multiply(V);
    }

    public ComplexMatrixDense getD() {
        return D;
    }

    public ComplexMatrixDense getV() {
        return V;
    }

    /**
     * Computes the Frobenius norm of a the submatrix (ii1:ii2, jj1,jj2)
     * of a Zmat.
     *
     * @param A   The zmat
     * @param ii1 The lower row index
     * @param ii2 The upper row index
     * @param jj1 The lower column index
     * @param jj2 The upper column index
     * @return The Frobenius norm of A(ii1:ii2, jj1:jj2)
     */
    private static double normFro(ComplexMatrixDense A, int ii1, int ii2, int jj1, int jj2) {
        int i, i1, i2, j, j1, j2;
        double fac, nrm, scale;

        i1 = ii1;
        i2 = ii2;
        j1 = jj1;
        j2 = jj2;

        scale = 0.0;
        for (i = i1; i <= i2; i++) {
            for (j = j1; j <= j2; j++) {
                scale = Math.max(scale,
                        Math.abs(A.unsafeGet(i, j).real()) + Math.abs(A.unsafeGet(i, j).imag()));
            }
        }
        if (scale == 0) {
            return 0.0;
        }
        if (scale < 1) {
            scale = scale * 1.0e20;
        }
        scale = 1 / scale;
        nrm = 0;
        for (i = i1; i <= i2; i++) {
            for (j = j1; j <= j2; j++) {
                fac = scale * A.unsafeGet(i, j).real();
                nrm = nrm + fac * fac;
                fac = scale * A.unsafeGet(i, j).imag();
                nrm = nrm + fac * fac;
            }
        }
        return Math.sqrt(nrm) / scale;
    }
}
