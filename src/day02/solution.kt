package day02

import utils.Skip
import utils.runDaySolutions

data class GameSet(val red: Int, val green: Int, val blue: Int) {
    companion object {
        fun from(line: String): GameSet {
            val balls = line.split(",")

            var red = 0
            var green = 0
            var blue = 0

            for (ball in balls) {
                if (ball.contains("red")) {
                    red = ball.trim().split(" ")[0].trim().toInt()
                }

                if (ball.contains("green")) {
                    green = ball.trim().split(" ")[0].trim().toInt()
                }

                if (ball.contains("blue")) {
                    blue = ball.trim().split(" ")[0].trim().toInt()
                }
            }

            return GameSet(red, green, blue)
        }
    }
}
data class Game(val id: Int, val sets: Array<GameSet>) {
    fun isPossible(): Boolean {
        for (set in sets) {
            if (set.red > 12 || set.green > 13 || set.blue > 14) {
                return false
            }
        }

        return true
    }

    fun getMinimalRequiredPower(): Int {
        return sets.map { it.red }.filter { it != 0 }.max() * sets.map { it.green }.filter { it != 0 }.max() * sets.map { it.blue }.filter { it != 0 }.max()
    }

    companion object {
        fun from(line: String): Game {
            val id = line.split(":")[0].takeLastWhile { it != ' ' }.toInt()
            val sets = line
                .split(":")[1]
                    .split(';')
                    .map {
                        GameSet.from(it)
                    }

            return Game(id, sets.toTypedArray())
        }
    }
}

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun part1(input: List<String>): Int {
        val games = input.map { Game.from(it) }

        return games.filter { it.isPossible() }.fold(0) { acc, cur -> acc + cur.id }
    }

    fun part2(input: List<String>): Long {
        val games = input.map { Game.from(it) }

        return games.fold(0L) { acc, cur -> acc + cur.getMinimalRequiredPower() }
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}
