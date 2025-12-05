package day05

import utils.runDaySolutions
import kotlin.math.min
import kotlin.text.toLong

data class Shift(val source: Long, val destination: Long, val shift: Long) {
    companion object {
        fun from(line: String): Shift {
            val split = line.split(" ")

            return Shift(split[1].toLong(), split[0].toLong(), split[2].toLong())
        }
    }
}

class Mapper(shifts: Array<Shift>) {
    private val shifts = shifts.sortedBy { it.source }

    fun getMapped(current: Long): Long {
        for (shift in shifts) {
            if (current in shift.source..<shift.source + shift.shift) {
                return current + shift.destination - shift.source
            }
        }

        return current
    }
}

class Mappers(private val mappers: Array<Mapper>) {
    fun getFinal(seed: Long): Long {
        var current = seed

        for (mapper in mappers) {
            current = mapper.getMapped(current)
        }

        return current
    }
}

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun parseSeeds(line: String): Array<Long> {
        return line.split(": ")[1].split(" ").map { it.toLong() }.toTypedArray()
    }

    fun parseMappers(input: List<String>): Mappers {
        val shifts = mutableListOf<Shift>()
        val mappers = mutableListOf<Mapper>()

        for (line in input) {
            if (line.isEmpty()) {
                if (shifts.isNotEmpty()) {
                    mappers += Mapper(shifts.toTypedArray())

                    shifts.clear()
                }

                continue
            }

            if (line[0] !in '0'..'9') {
                continue
            }

            shifts += Shift.from(line)
        }

        if (shifts.isNotEmpty()) {
            mappers += Mapper(shifts.toTypedArray())
        }

        return Mappers(mappers.toTypedArray())
    }

    fun part1(input: List<String>): Long {
        var result = Long.MAX_VALUE

        val seeds = parseSeeds(input[0])
        val mappers = parseMappers(input.takeLast(input.size - 2))

        for (seed in seeds) {
            val final = mappers.getFinal(seed)

            result = min(result, final)
        }

        return result
    }

    fun parseSeedsRanges(line: String): Array<LongRange> {
        val nums = line.split(": ")[1].split(" ")

        val seeds = mutableListOf<LongRange>()

        var left: Long = 0

        for (i in nums.indices) {
            if (i % 2 == 1) {
                val right = left + nums[i].toLong()

                seeds += LongRange(left, right)
            } else {
                left = nums[i].toLong()
            }
        }

        return seeds.toTypedArray()
    }

    fun part2(input: List<String>): Long {
        var result = Long.MAX_VALUE

        val seeds = parseSeedsRanges(input[0])
        val mappers = parseMappers(input.takeLast(input.size - 2))

        var count = 0L
        var total = 0L

        for (seedRange in seeds) {
            total += seedRange.last - seedRange.first
        }

        for (seedRange in seeds) {
            for (seed in seedRange) {
                val final = mappers.getFinal(seed)

                count += 1

                if (count % 100000L == 0L) {
                    println("$count / $total")
                }

                result = min(result, final)
            }
        }

        return result
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}



