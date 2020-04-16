package com.example.statisticalpredictionmodellingapplication.Kotlin

class Feasible {
    lateinit var coefficients: Matrix
    lateinit var xvar: Matrix

    var z = 0

    fun getSolution(coeffs: Matrix, Constraints: Matrix, minimums: Matrix): Feasible {
        lateinit var result: Feasible

        var matrix = Matrix() //Use this to get functions from Matrix class

        var n = Constraints.size[0]
        var m = Constraints.size[1]

        //Create new matrices B, N, c_B and c_N
        var B = matrix.identity(n)
        var N = Constraints

        var c_B = matrix.zero(n, 1)
        var c_N = coeffs

        var cond = -1

        while(cond < 0) {
            var Binv = B.inverse()

            var enter = 0
            var preventer: Int
            var leave = 0

            var zz: Int
            var prevzz = -1

            for(j in 0..m) {
                zz = (c_B.transpose() * Binv * N[j, 'c'])() - c_N[j, 'r']()
                if(zz < 0 && zz < prevzz) {
                    prevzz = zz
                    preventer = enter
                    enter = j
                }
                else if(zz >= 0) {
                    //Solution is optimal
                    cond = zz

                    result.coefficients = c_B
                    result.xvar = Binv * minimums
                    result.z = zz

                    return result
                }
            }

            var nextvar = Binv * minimums
            var p = Int.MAX_VALUE
            for(k in 0..n){
                if(nextvar[0, k] in 0..p){
                    p = k
                    leave = k
                }
                else if(nextvar[0, k] < 0){
                    matrix.fail("ERROR: Computational error! Consider revising function parameters")
                }
            }

            //ToDo: swap values
            matrix.swap_column(N, B, enter, leave)
            matrix.swap_row(c_N, c_B, enter, leave)
        }

        return result
    }
}