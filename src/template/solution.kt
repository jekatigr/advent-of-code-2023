package template

import utils.Skip
import utils.runDaySolutions

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun part1(input: List<String>): Int {
        var result = 0

        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0

        return result
    }

    runDaySolutions(day, ::part1, ::part2,  setOf(Skip.PART2_TESTS, Skip.PART2_SOLUTION))
}
