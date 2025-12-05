package day06

import utils.runDaySolutions

class Race(val timeLimit: Long, val record: Long) {
    constructor(timeLimit: Int, record: Int): this(timeLimit.toLong(), record.toLong())

    fun getNumberOfWinningTimes(): Int {
        var count = 0

        for (time in 1..timeLimit) {
            val left = timeLimit - time
            if (left * time > record) {
                count += 1
            }
        }

        return count
    }
}

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun parseRaces(input: List<String>): List<Race> {
        val times = input[0].removePrefix("Time:").trim().split(" ").filter { it != "" }.map{ it.toInt() }
        val distances = input[1].removePrefix("Distance:").trim().split(" ").filter { it != "" }.map{ it.toInt() }

        val races = mutableListOf<Race>()

        for (i in times.indices) {
            races.add(Race(times[i], distances[i]))
        }

        return races
    }

    fun parseSingleRace(input: List<String>): Race {
        val time = input[0].removePrefix("Time:").trim().split("").filter { it != " " && it != "" }.joinToString("").toLong()
        val distance = input[1].removePrefix("Distance:").trim().split("").filter { it != " " && it != "" }.joinToString("").toLong()

        return Race(time, distance)
    }

    fun part1(input: List<String>): Int {
        val races = parseRaces(input)

        return races.fold(1) { acc, cur -> acc * cur.getNumberOfWinningTimes() }
    }

    fun part2(input: List<String>): Int {
        val race = parseSingleRace(input)

        return race.getNumberOfWinningTimes()
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}
