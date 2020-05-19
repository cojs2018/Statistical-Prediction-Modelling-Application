package com.example.statisticalpredictionmodellingapplication.Kotlin

class Feasible {
    lateinit var coefficients: Matrix
    lateinit var xvar: Matrix

    var z = 0

    constructor() {
        coefficients = Matrix()
        xvar = Matrix()
        z = 0
    }

    fun getSolution(coeffs: Matrix, Constraints: Matrix, minimums: Matrix): Feasible {
        var result = Feasible()

        var matrix = Matrix() //Use this to get functions from Matrix class

        var n = Constraints.size[0]
        var m = Constraints.size[1]

        //Create new matrices B, N, c_B and c_N
        var B = Matrix().identity(n)
        var N = Constraints

        var c_B = Matrix().zero(n, 1)
        var c_N = coeffs

        var cond = -1

        while(cond < 0) {
            var Binv = B.inverse()

            var enter = 0
            var preventer: Int
            var leave = 0

            var zz: Int
            var prevzz = -1

            for(j in 0 until m) {
                try {
                    zz = (c_B.transpose() * Binv * N[j, 'c'])() - c_N[j, 0]
                    if (zz < 0 && zz < prevzz) {
                        prevzz = zz
                        preventer = enter
                        enter = j
                    } else if (zz >= 0) {
                        //Solution is optimal
                        cond = zz

                        result.coefficients = c_B
                        result.xvar = Binv * minimums
                        result.z = zz

                        return result
                    }
                }
                catch(exe: Exception) {
                    enter = j
                    break
                }
            }

            var nextvar = Binv * minimums
            var p = Int.MAX_VALUE
            for(k in 0 until n){
                if(nextvar[k, 0] in 0..p){
                    p = nextvar[k, 0]
                    leave = k
                }
                else if(nextvar[k, 0] < 0){
                    matrix.fail("ERROR: Computational error! Consider revising function parameters")
                }
            }

            //ToDo: swap values
            Matrix().swap_column(N, B, enter, leave)
            Matrix().swap_row(c_N, c_B, enter, leave)
        }

        return result
    }
}