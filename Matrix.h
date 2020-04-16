#pragma once

#include <vector>

using namespace std;

namespace MATH
{
	struct feasible_solution
	{
		Matrix X;
		Matrix C;

		int z;

		feasible_solution() = default;
	};

	feasible_solution linear_program(Matrix C, Matrix M, Matrix v);

	void swap_columns(Matrix A, Matrix B, int enter, int leave);

	void swap_rows(Matrix A, Matrix B, int enter, int leave);

	class Matrix
	{
	public:
		vector<vector<int>> matrix; //Contains matrix data
		int size[2]; //dimentions of matrix

		Matrix() = default;

		Matrix(int n, int m, int value = 0) { //Initalise matrix with all elements set to a constant value;
			size[0] = n; size[1] = m;

			for (int i = 0; i < n; i++) {
				vector<int> row;
				for (int j = 0; j < m; j++) {
					row.push_back(value);
				}
				matrix.push_back(row);
			}
		}

		Matrix(vector<vector<int>> M) { //Fill matrix with data from two dimentional array
			matrix = M;
			size[0] = M.size(); size[1] = M.at(0).size();
		}

		Matrix operator >> (Matrix const& obj) { //Post-multiplication of matrices
			int n0 = size[0]; int m0 = size[1];
			int n1 = obj.size[0]; int m1 = obj.size[1];

			if (n1 == m0) { //If two matrices correspond
				Matrix result = Matrix(n0, m1, 0);

				for (int i = 0; i < n0; i++) {
					for (int j = 0; j < m1; j++) {
						for (int ii = 0; i < m0; ii++) {
							for (int jj = 0; jj < n1; jj++) {
								result.matrix.at(i).at(j) = matrix.at(i).at(jj) + obj.matrix.at(ii).at(j);
							}
						}
					}
				}

				return result;
			}
			else {
				throw "Matrices A (dim: n x m) and B (dim: m x p) must correspond accordingly for multiplication to take place";
			}
		}

		Matrix operator << (Matrix const& obj) { //Pre-multiplication of matrices
			int n0 = obj.size[0]; int m0 = obj.size[1];
			int n1 = size[0]; int m1 = size[1];

			if (n1 == m0) { //If two matrices correspond
				Matrix result = Matrix(n0, m1, 0);

				for (int i = 0; i < n0; i++) {
					for (int j = 0; j < m1; j++) {
						for (int ii = 0; i < m0; ii++) {
							for (int jj = 0; jj < n1; jj++) {
								result.matrix.at(i).at(j) = obj.matrix.at(i).at(jj) + matrix.at(ii).at(j);
							}
						}
					}
				}

				return result;
			}
			else {
				throw "Matrices A (dim: n x m) and B (dim: m x p) must correspond accordingly for multiplication to take place";
			}
		}

		Matrix operator + (Matrix const& obj) { //Addition of matrices
			int n0 = size[0]; int m0 = size[1];
			int n1 = obj.size[0]; int m1 = obj.size[1];

			if (n0 == n1 && m0 == m1) { //If two matrices correspond
				Matrix result = Matrix(n0, m1, 0);

				for (int i = 0; i < n0; i++) {
					for (int j = 0; j < m1; j++) {
						result.matrix.at(i).at(j) = matrix.at(i).at(j) + obj.matrix.at(i).at(j);
					}
				}

				return result;
			}
			else {
				throw "Matrices A and B must have same dimentions for addition to take place";
			}
		}

		Matrix operator * (int const& obj) {
			int n = matrix.size[0]; int m = matrix.size[1];

			Matrix result = Matrix(n, m, 0);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					result.matrix.at(i).at(j) = obj * matrix.at(i).at(j);
				}
			}

			return result;
		}

		Matrix operator - (Matrix const& obj); /*{
			return this + (obj * (-1));
		}*/

		Matrix operator ^ (int const& obj) {
			if (obj > 1) {
				return *this >> (*this ^ (obj - 1));
			}
			else if (obj == 1) {
				return *this;
			}
			else if (obj == 0) {
				return identity(size[0]);
			}
			else if (obj == -1) {
				return this->inverse();
			}
			else { //If obj < -1
				return (this->inverse()) ^ (-1 * obj);
			}
		}

		Matrix identity(int n) { //Returns identity square matrix of dimentions n x n
			Matrix I = Matrix(n, n, 0);
			for (int i = 0; i < n; i++) {
				I.matrix.at(i).at(i) = 1;
			}
			return I;
		}

		Matrix inverse() { //Get inverse of matrix using Gaussian elimination
			Matrix A = *this;

			if (A.size[0] == A.size[1]) { //If matrix A is square
				Matrix I = identity(A.size[0]);

				int n = A.size[0];

				for (int i = 0; i < n - 1; i++) { //Get Echilon form
					int a_ii = A.matrix.at(i).at(i);
					for (int j = i; j < n; j++) {
						int a_ij = A.matrix.at(i).at(j);
						for (int k = i; k < n; k++) {
							A.matrix.at(j).at(k) += -1 * (a_ij / a_ii) * A.matrix.at(i).at(k);
							I.matrix.at(j).at(k) += -1 * (a_ij / a_ii) * I.matrix.at(i).at(k);
						}
					}
				}

				for (int i = n; i >= 0; i--) {
					int a_ii = A.matrix.at(i).at(i);
					if (i > 0) {
						for (int j = i; j >= 0; j--) {
							int a_ij = A.matrix.at(i).at(j);
							for (int k = i; k >= 0; k--) {
								A.matrix.at(j).at(k) += -1 * (a_ij / a_ii) * A.matrix.at(i).at(k);
								I.matrix.at(j).at(k) += -1 * (a_ij / a_ii) * I.matrix.at(i).at(k);
							}
						}
					}
					for (int c = i; c < n; c++) {
						A.matrix.at(i).at(c) *= (1 / a_ii);
					}
				}

				return A;
			}
			else {
				throw "Matrix must be square. (dimentions n x n)";
			}
		}

		Matrix transpose() { //Get transpose of matrix
			Matrix A = *this;
			Matrix T = Matrix(A.size[1], A.size[0], 0);

			for (int i = 0; i < A.size[1]; i++) {
				for (int j = 0; j < A.size[0]; j++) {
					T.matrix.at(i).at(j) = A.matrix.at(j).at(i);
				}
			}

			return T;
		}

		int at(int const& i, int const& j) { //return item at row i and column j
			return matrix.at(i).at(j);
		}

		Matrix atr(int i) { //Return row i
			vector<int> row; //Create empty row
			Matrix result = Matrix(1, size[1], 0);

			for (int j = 0; j < size[1]; j++) {
				row.push_back(matrix.at(i).at(j));
			}

			result.matrix.push_back(row);
			return result;
		}

		Matrix atc(int j) { //Return column j
			Matrix result = Matrix(size[0], 1, 0);

			for (int i = 0; i < size[0]; i++) {
				vector<int> element;

				element.push_back(matrix.at(i).at(j));
				result.matrix.push_back(element);
			}

			return result;
		}
	};
}		