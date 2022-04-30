package com.fifteenpuzzle.ui.main

class Position(var x: Float, var y: Float) {
    // subtracts one position from another and
    // returns a new position as a result
    private fun subtract(p1: Position, p2: Position): Position {
        return Position(p1.x - p2.x, p1.y - p2.y)
    }
    // adds one position to another, and returns a new position as a result
    private fun add(p1: Position, p2: Position): Position {
        return Position(p1.x + p2.x, p1.y + p2.y)
    }

    // this checks if the position is valid in my grid
    private fun isValid(matrixSize: Int): Boolean {
        return (this.x in 0f..matrixSize-1f && this.y in 0f..matrixSize-1f)
    }

    // uses add and subtract and valid above to return
    // a list of valid neigbors to this Position
    fun neighbors(matrixSize: Int): List<Position> {
        val neighbors = mutableListOf<Position>()

        neighbors += subtract(this, Position(0f, 1f))
        neighbors += subtract(this, Position(1f, 0f))
        neighbors += add(this, Position(0f, 1f))
        neighbors += add(this, Position(1f, 0f))
        return neighbors.filter { n -> n.isValid(matrixSize) }
    }

    // this overrides the subtraction operator
    // so that I can use the - sign
    // to subtract two positions
    operator fun minus(p2: Position): Position {
        // calculates the subtraction with this Position minus the other
        val p1 = Position(this.x - p2.x, this.y - p2.y)
        // calculates the subtraction with the other position minus this one
        val p3 = Position(p2.x - this.x, p2.y - this.y)
        //return whichever position has positive coordinates
        return if (p1.x > 0 && p1.y > 0)
            p1
        else p3
    }

    override fun toString(): String {
        return "P(x: $x, y: $y)"
    }

    // Overriden to compare two positions, if need be
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    // needed when equals method is overloaded.
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}