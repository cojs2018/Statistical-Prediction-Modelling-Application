#include "Matrix.h"

using namespace MATH;

MATH::feasible_solution MATH::linear_program(Matrix C, Matrix M, Matrix v)
{/*  Function to generate feasible solution to the following problem:
  *  Maximise z = cx subject to Mx = v
  */
	feasible_solution result;

	int n = M.size[0]; int m = M.size[1];

	//Create new matrices, B and N, c_B and c_N
	Matrix B = M.identity(n);
	Matrix N = M;

	Matrix c_B = Matrix(n, 1, 0);
	Matrix c_N = C;

	int cond = -1;

	while (cond < 0) {
		Matrix Binv = B ^ (-1);

		int enter = 0; //Entering variable
		int prev_enter = 0; //Previous entering value

		int leave = 0; //Leaving variable

		int zz;
		int prev_zz = -1;


		for (int j = 0; j < m; j++) { //Get index of entering variable
			zz = (c_B.transpose() >> Binv >> N.atc(j)).at(0, 0) - c_N.at(j, 0);

			if (zz < prev_zz && zz < 0) {
				prev_zz = zz;
				prev_enter = enter;
				enter = j;
			}
			else if (zz > 0) {
				cond = zz;

				//Process result
				result.C = c_B;
				result.X = Binv >> v;
				result.z = (result.C >> result.X).at(0, 0);

				break;
			}
		}

		Matrix next = Binv.operator>>(v);
		int prev_next = 100000000;
		for (int k = 0; k < n; k++) { //Get index of entering variable;
			if (next.at(k, 0) < prev_next) {
				prev_next = next.at(k, 0);
				leave = k;
			}
		}

		swap_columns(B, N, enter, leave);
		swap_rows(c_B, c_N, enter, leave);
	}

	return result;
}

void MATH::swap_columns(Matrix A, Matrix B, int enter, int leave)
{//Function to swap columns in two matrices

	//First get column leave from A, call it temp0
	Matrix temp0 = A.atc(leave);
	//And get column enter from B, call it temp1
	Matrix temp1 = B.atc(enter);

	//Set enter column in B to temp0
	int n = temp0.size[0];

	for (int i = 0; i < n; i++) {
		B.matrix.at(i).at(enter) = temp0.at(i, 0);
	}

	//Set leave column in A to temp1
	n = temp1.size[0];

	for (int i = 0; i < n; i++) {
		A.matrix.at(i).at(leave) = temp1.at(i, 0);
	}

}

void MATH::swap_rows(Matrix A, Matrix B, int enter, int leave)
{//Function to swap rows in two matrices

	//First get row leave from A, call it temp0
	Matrix temp0 = A.atr(leave);
	//And get row enter from B, call it temp1
	Matrix temp1 = B.atr(enter);

	//Set enter row in B to temp0
	int m = temp0.size[1];

	for (int j = 0; j < m; j++) {
		B.matrix.at(enter).at(j) = temp0.at(0, j);
	}

	//Set leave row in A to temp1
	m = temp1.size[1];

	for (int j = 0; j < m; j++) {
		A.matrix.at(leave).at(j) = temp1.at(0, j);
	}

}
