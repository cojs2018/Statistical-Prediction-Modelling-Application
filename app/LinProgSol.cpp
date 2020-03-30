//
// Created by User on 30/03/2020.
//


#include "LinProgSol.h"

using namespace Feasible;
using namespace Eigen;
using namespace std;

public solution program::linear(VectorXi Coeffs, MatrixXi Contstraints, Vector2i Minimums)
{
    solution result = new solution();

    int n = 2;
    int m = 6;

    //Create new matrices, B and N, c_B and c_N
    Matrix2i B = Matrix2i::Identity();
    MatrixXi N = Constraints;

    Vector2i C_B;
    C_B << 0,
           0;

    VectorXi C_N = Coeffs;

    int cond = -1;

    while(cond < 0) {
        Matrix2i Binv = B.inverse();

        int enter = 0; //Entering variable
        int prev_enter; //Previous entering variable

        int leave = 0; //Leaving variable

        int zz;
        int prev_zz = -1;

        for(int j = 0; j < m; j++) { //Get index of entering variable
            zz = (C_B.transpose() * Binv * N.columns(j)) - C_N(j);

            if(zz < prev_zz && zz < 0) {
                prev_z = zz;
                prev_enter = enter;
                enter = j;
            }
            else if (zz >= 0) {
                cond = zz;

                //Process result
                result.Coefficients = C_B;
                result.Xvar = Binv * Minimums;
                result.z = zz;

                return result;
            }
        }

        Vector2i NextVar = Binv * Minimums;
        int p = 100000000000;
        for(int k = 0; k < n; k++) { //Get index of leaving variable
            if(NextVar(k) < p) {
                p = NextVar(k);
                leave = k;
            }
        }


        this.swap_columns(B, N, enter, leave);
        this.swap_rows(C_B, C_N, enter, leave);
    }
}

public void swap_columns(MatrixXi A, MatrixXi B, int x, int y)
{ //Function swaps columns across matrices A and B

    //First get y variable from A
    auto temp_A_at_y = A.columns(y);
    //and get x variable from B
    auto temp_B_at_x = B.columns(x);

    //swap in corresponding positions
    A.columns(y) = temp_B_at_x;
    B.columns(x) = temp_A_at_y;
}

public void swap_rows(VectorXi A, VectorXi B, int x, int y)
{//Function swaps rows across matrices A and B

    //First get y variable from A
    auto temp_A_at_y = A(y);
    //and get x variable from B
    auto temp_B_at_x = B(x);

    //swap in corresponding positions
    A(y) = temp_B_at_x;
    B(x) = temp_A_at_y;
}