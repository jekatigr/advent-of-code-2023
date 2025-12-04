package day03

import utils.Matrix
import utils.Point
import utils.Side
import utils.Skip
import utils.runDaySolutions
import kotlin.collections.sortWith

class Engine(val schematic: Matrix<Char>) {
    fun getPartsNumbers(): Array<Int> {
        val result = mutableListOf<Int>()

        for (i in schematic.heightRange()) {
            var j = 0

            while (j < schematic.width()) {
                if (schematic.get(i, j).digitToIntOrNull() == null) {
                    j += 1

                    continue
                }

                var partNumber = ""
                var hasSymbolNearby = false

                while (j < schematic.width() && schematic.get(i, j).digitToIntOrNull() != null) {
                    partNumber += schematic.get(i, j)

                    if (hasSymbolNearby) {
                        j += 1

                        continue
                    }

                    val neighborsSymbol = schematic.getNeighbours(i, j)
                        .find { schematic.get(it)
                            .let {
                                value -> value.digitToIntOrNull() == null && value != '.'
                            }
                        }

                    if (neighborsSymbol != null) {
                        hasSymbolNearby = true
                    }

                    j += 1
                }

                if (hasSymbolNearby) {
                    result.add(partNumber.toInt())
                }

                j += 1
            }
        }

        return result.toTypedArray()
    }

    fun getNumberPartIndexRange(p: Point): IntRange {
        var left = p
        var right = p

        while (schematic.isValidCoordinates(Side.LEFT.from(left)) && schematic.get(Side.LEFT.from(left)).digitToIntOrNull() != null) {
            left = Side.LEFT.from(left)
        }

        while (schematic.isValidCoordinates(Side.RIGHT.from(right)) && schematic.get(Side.RIGHT.from(right)).digitToIntOrNull() != null) {
            right = Side.RIGHT.from(right)
        }

        return IntRange(left.second, right.second)
    }

    fun getNumberByRange(row: Int, range: IntRange): Int {
        var str = ""
        for (i in range) {
            str += schematic.get(row, i)
        }

        return str.toInt()
    }

    fun mergeRanges(ranges: MutableList<IntRange>): List<IntRange> {
        val rangesMerged = mutableListOf<IntRange>()

        ranges.sortWith { r1, r2 -> r1.first - r2.first }

        for (range in ranges) {
            if (rangesMerged.isEmpty() || rangesMerged.last().last < range.first) {
                rangesMerged += range

                continue
            }

            val last = rangesMerged.removeLast()

            rangesMerged += IntRange(last.first, range.last)
        }

        return rangesMerged
    }

    fun getGearsRatios(): Long {
        var ratios = 0L

        val gears = schematic.findAll('*')

        for (gear in gears) {
            val numbers = mutableListOf<Int>()

            // left and right
            for (point in setOf(Side.LEFT.from(gear), Side.RIGHT.from(gear))) {
                if (!schematic.isValidCoordinates(point) || schematic.get(point).digitToIntOrNull() == null) {
                    continue
                }

                val range = getNumberPartIndexRange(point)

                numbers += getNumberByRange(gear.first, range)
            }

            // top
            var ranges = mutableListOf<IntRange>()
            for (point in setOf(Side.UP.from(gear), Side.UP_LEFT.from(gear), Side.UP_RIGHT.from(gear))) {
                if (!schematic.isValidCoordinates(point) || schematic.get(point).digitToIntOrNull() == null) {
                    continue
                }

                ranges += getNumberPartIndexRange(point)
            }

            numbers.addAll(mergeRanges(ranges).map { getNumberByRange(gear.first - 1, it) })

            // bottom
            ranges = mutableListOf<IntRange>()
            for (point in setOf(Side.DOWN.from(gear), Side.DOWN_RIGHT.from(gear), Side.DOWN_LEFT.from(gear))) {
                if (!schematic.isValidCoordinates(point) || schematic.get(point).digitToIntOrNull() == null) {
                    continue
                }

                ranges += getNumberPartIndexRange(point)
            }

            numbers.addAll(mergeRanges(ranges).map { getNumberByRange(gear.first + 1, it) })

            if (numbers.size != 2) {
                continue
            }

            ratios += numbers[0] * numbers[1]
        }

        return ratios
    }
}

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun part1(input: List<String>): Int {
        val engine = Engine(Matrix.from(input, '.') { it })

        return engine.getPartsNumbers().sumOf { it }
    }

    fun part2(input: List<String>): Long {
        val engine = Engine(Matrix.from(input, '.') { it })

        return engine.getGearsRatios()
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}
