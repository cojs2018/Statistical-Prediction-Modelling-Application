package com.example.statisticalpredictionmodellingapplication.Kotlin

import java.util.*
import kotlin.collections.ArrayList

class Matrix {
    var data = ArrayList<ArrayList<Int>>() //Must be of a numeric data type
    var size = IntArray(2)

    constructor() { /* Default constructor */ }

    fun setMatrix(v: Vector<Int>, cutoff: Int): Matrix {
        val m = cutoff
        val n = v.size / cutoff

        var matrix = zero(n, m);
        var it = 0 //iterator
        while(it < m*n) {
            var i = it / m
            var j = it % m

            var x = v.elementAt(it).toInt()
            matrix[i, j] = x

            it++
        }

        return matrix
    }

    fun zero(n: Int, m: Int): Matrix {
        //Creates a zero matrix of dimensions n x m

        for(i in 0..n) {
            var row = ArrayList<Int>()
            for (j in 0..m) {
                row.add(0)
            }
            this.data.add(row)
        }

        this.size[0] = n
        this.size[1] = m

        return this
    }

    fun ones(n: Int, m: Int): Matrix {
        //Creates a one matrix of dimensions n x m

        for(i in 0..n) {
            var row = ArrayList<Int>()
            for (j in 0..m) {
                row.add(1)
            }
            this.data.add(row)
        }

        this.size[0] = n
        this.size[1] = m

        return this
    }

    fun identity(n: Int): Matrix { //Returns identity matrix of dimensions n x n
        var Res = zero(n, n)

        for(i in 0..n) {
            Res[i, i] = 1
        }

        return Res
    }

    operator fun get(i: Int, j: Int): Int {
        return this.data[i][j]
    }

    operator fun get(j: Int, cORr: Char): Matrix { //Get column j from matrix
        val n = this.size[0]
        val m = this.size[1]

        var vector: Matrix

        if(cORr == 'c') {
            vector = zero(n, 1)

            for (i in 0..n) {
                vector[i, j] = this[i, j]
            }
        }
        else if(cORr == 'r') {
            vector = zero(1, m)

            for (i in 0..m) {
                vector[j, i] = this[j, i]
            }
        }
        else {
            fail("ERROR: Must chose either a column or row!")
        }

        return vector
    }

    operator fun set(i:Int, j: Int, x: Int) {
        this.data[i][j] = x
    }

    operator fun set(j: Int, cORr: Char, Obj: Matrix) {
        var n = this.size[0]
        val m = this.size[1]

        var vector: Matrix

        if(cORr == 'c') {

            for (i in 0..n) {
                this[i, j] = Obj[i, j]
            }
        }
        else if(cORr == 'r') {

            for (i in 0..m) {
                this[j, i] = this[j, i]
            }
        }
        else {
            fail("ERROR: Must chose either a column or row!")
        }
    }

    operator fun invoke(): Int { //Get a determinant
        val n = this.size[0]

        val adjsign: (Int) -> Int = {x: Int ->
            if(x % 2 == 0)
                1
            else {
                -1
            }
        }

        if(n == this.size[1]) {
            if(n == 1) {
                return this[1, 1]
            }
            else {
                var det = 0
                for(j in 0..n) {
                    det += adjsign(j) * this[0, j] * this(0, j).invoke()
                }

                return det
            }
        }
        else {
            fail("ERROR: Matrix must be square")
        }

        return 0
    }

    operator fun invoke(r: Int, c: Int): Matrix { //Get new matrix excluding column j and row i
        val n = this.size[0]
        val m = this.size[1]

        var Res = zero(n-1, m-1)

        for(i in 0..r) {
            for(j in 0..c) {
                Res[i, j] = this[i, j]
            }
            for(j in (c+1)..m) {
                Res[i, j-1] = this[i, j]
            }
        }
        for(i in (r+1)..n) {
            for(j in 0..c) {
                Res[i-1, j] = this[i, j]
            }
            for(j in (c+1)..m) {
                Res[i-1, j-1] = this[i, j]
            }
        }

        return Res
    }

    operator fun plus(Obj: Matrix): Matrix {
        val n = this.size[0]
        val m = this.size[1]

        var Res = zero(n, m)

        if(n == Obj.size[0] && m == Obj.size[1]) {
            for(i in 0..n) {
                for(j in 0..m){
                    Res[i, j] = this[i, j] + Obj[i, j]
                }
            }
        }
        else {
            fail("ERROR: Matrices must be of the same size for addition and subtraction!")
        }

        return Res
    }

    operator fun minus(Obj: Matrix): Matrix {
        val n = this.size[0]
        val m = this.size[1]

        var Res = zero(n, m)

        if(n == Obj.size[0] && m == Obj.size[1]) {
            for(i in 0..n) {
                for(j in 0..m){
                    Res[i, j] = this[i, j] - Obj[i, j]
                }
            }
        }
        else {
            fail("ERROR: Matrices must be of the same size for addition and subtraction!")
        }

        return Res
    }

    operator fun times(Obj: Matrix): Matrix {
        val n = this.size[0]
        val m = this.size[1]

        val nb = Obj.size[0]
        val mb = Obj.size[1]

        var Res = zero(n, mb)

        if(m == nb) {
            for(i in 0..n) {
                for(jb in 0..mb) {
                    for(ib in 0..nb) {
                        for(j in 0..m) {
                            Res[i, jb] += this[i, j] * Obj[ib, j]
                        }
                    }
                }
            }
        }
        else {
            fail("ERROR: Matrices A (n0 x m0) and B (n1 x m1) must correspond (m0 = n1) for multiplication to take place!")
        }

        return Res
    }

    operator fun times(obj: Int): Matrix { //Scalar multiplication
        val n = this.size[0]
        val m = this.size[1]

        var Res = zero(n, m)

        for(i in 0..n) {
            for(j in 0..m) {
                Res[i, j] = obj * this[i, j]
            }
        }

        return Res
    }

    fun transpose(): Matrix { //Transpose matrix
        val n = this.size[0]
        val m = this.size[1]

        var Res = zero(m, n)

        for(i in 0..n) {
            for(j in 0..m) {
                Res[j, i] = this[i, j]
            }
        }

        return Res
    }

    fun power(obj: Int): Matrix {
        if(this.size[0] == this.size[1]) {
            if (obj > 1) {
                return (this * this.power(obj - 1))
            } else if (obj == 1) {
                return this
            } else if (obj == 0) {
                return identity(this.size[0])
            } else if (obj == -1) {
                return this.inverse()
            } else if (obj < -1) {
                return this.inverse().power(-1 * obj)
            }
        }
        else {
            fail("ERROR: Matrix must be square!")
        }

        return zero(0, 0)
    }

    fun inverse(): Matrix {
        val n = this.size[0]
        val m = this.size[1]

        var Res = identity(n) //Identity matrix

        if(n == m) {
            if(this() != 0) {
                var A = this

                var i = 0
                while(i < (n - 1)) { //Get Echilon form
                    val aii = A[i, i]
                    var j = 0
                    while(j < n) {
                        val aij = A[i, j]
                        var k = 0
                        while(k < n) {
                            A[i, j] += -1 * (aii / aij) * A[i, k]
                            Res[i, j] += -1 * (aii / aij) * Res[i, k]
                            k++
                        }
                        j++
                    }
                    i++
                }

                while(i > 0) { //Get reduced Echilon form
                    val aii = A[i, i]
                    var j = i
                    while(j > 1) {
                        val aij = A[i, j]
                        var k = 0
                        while(k < n) {
                            A[i, j] += -1 * (aii / aij) * A[i, k]
                            Res[i, j] += -1 * (aii / aij) * Res[i, k]
                            k++
                        }
                        j--
                    }
                    var c = 0
                    while(c < n) {
                        A[i, c] *= (1 / aii)
                        Res[i, c] *= (1 / aii)
                        c++
                    }
                    i--
                }
            }
            else {
                fail("ERROR: Depreciated matrix!")
            }
        }
        else {
            fail("ERROR: Matrix must be square!")
        }

        return Res
    }

    fun swap_column(A: Matrix, B: Matrix, j0: Int, j1: Int) { //Swap A[column: j0] and B[column: j1]
        var temp0 = A[j0, 'c']
        var temp1 = B[j1, 'c']

        A[j0, 'c'] = temp1
        B[j1, 'c'] = temp0
    }

    fun swap_row(A: Matrix, B: Matrix, i0: Int, i1: Int) {
        var temp0 = A[i0, 'r']
        var temp1 = B[i1, 'r']

        A[i0, 'r'] = temp1
        B[i1, 'r'] = temp0
    }

    fun index_times(Obj: Matrix): Matrix { //Multiplies in each index rather than in matrix multiplication
        val n = this.size[0]
        val m = this.size[0]

        var Res = zero(n, m)

        if(n == Obj.size[0] && m == Obj.size[1]) {
            for(i in 0..n)
                for(j in 0..m)
                    Res[i, j] = this[i, j] * Obj[i, j]
        }
        else {
            fail("ERROR: This operation can only be done on matrices of the same size!")
        }

        return Res
    }

    fun dot(Obj: Matrix): Int {
        val n = this.size[0]
        val m = this.size[0]

        var Res = 0

        var pred = (m == 1) && (Obj.size[1] == 1) && (n == Obj.size[0])

        if(pred) {
            Res = (this.transpose() * Obj)()
        }
        else {
            fail("ERROR: Dot and cross products can only be done between two vectors of the same size!")
        }

        return Res
    }

    fun cross(Obj: Matrix): Matrix {
        val n = this.size[0]
        val m = this.size[0]

        var Res = zero(n, 1)

        var pred = (m == 1) && (Obj.size[1] == 1) && (n == Obj.size[0])

        if(pred) {
            for(i in 0..n step 3) {
                var I = zero(2, 2)
                var J = zero(2, 2)
                var K = zero(2, 2)

                I[0, 0] = this[i+1, 0]
                I[1, 0] = this[i+2, 0]
                I[0, 1] = Obj[i+1, 0]
                I[1, 1] = Obj[i+2, 0]

                J[0, 0] = this[i, 0]
                J[1, 0] = this[i+2, 0]
                J[0, 1] = Obj[i, 0]
                J[1, 1] = Obj[i+2, 0]

                K[0, 0] = this[i, 0]
                K[1, 0] = this[i+1, 0]
                K[0, 1] = Obj[i, 0]
                K[1, 1] = Obj[i+1, 0]

                Res[i, 1] = I()
                Res[i+1, 1] = J()
                Res[i+2, 1] = K()
            }
        }
        else {
            fail("ERROR: Dot and cross products can only be done between two vectors of the same size!")
        }

        return Res
    }

   /* override fun toString(): String { //Converts matrix to string format
        val n = this.size[0]
        val m = this.size[1]

        var Res = ""

        for(i in 0..n) {
            for(j in 0..m) {
                Res += this[i, j].toString() + "  "
            }
            Res += "\n"
        }

        return Res
    }*/

    fun fail(message: String): Nothing {
        throw IllegalAccessException(message)
    }
}