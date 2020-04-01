//
// Created by User on 30/03/2020.
//

#include <Eigen/Dense>

#ifndef STATISTICALPREDICTIONMODELLINGAPPLICATION_LINPROGSOL_H
#define STATISTICALPREDICTIONMODELLINGAPPLICATION_LINPROGSOL_H

using namespace Eigen;

namespace Feasible
{
    class solution
    {
    public:

        Vector2i Coeffients;
        Matrix2i Xvar;

        int z;

        solution() = default;
    }

    class program
    {
    public:
        solution linear(VectorXi, MatrixXi, Vector2i);

        void swap_rows(VectorXi, VectorXi, int, int);
        void swap_columns(MatrixXi, MatrixXi, int, int);
    }
}

#endif STATISTICALPREDICTIONMODELLINGAPPLICATION_LINPROGSOL_H
