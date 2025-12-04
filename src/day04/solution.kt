package day04

import utils.runDaySolutions
import kotlin.math.pow

class Card(val winning: Set<Int>, val scratched: Set<Int>) {

    fun getMatchesCount() = scratched.count { it in winning }

    fun getPoints(): Int {
        val matched = getMatchesCount()

        if (matched == 0) return 0

        return 2.0.pow(matched.toDouble() - 1).toInt()
    }

    companion object {
        fun from(line: String): Card {
            val parts = line.split(":")[1].split("|")

            return Card(
                parts[0].split(" ").filter { it != ""}.map { it.trim().toInt() }.toSet(),
                parts[1].split(" ").filter { it != ""}.map { it.trim().toInt() }.toSet()
            )
        }
    }
}

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun part1(input: List<String>): Int {
        val cards = input.map { Card.from(it) }

        return cards.fold(0) { acc, card -> acc + card.getPoints() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { Card.from(it) }

        val counts = Array(cards.size) { 1 }

        for ((index, card) in cards.withIndex()) {
            val matched = card.getMatchesCount()

            if (matched == 0) {
                continue
            }

            for (i in index + 1..index + matched) {
                counts[i] += counts[index]
            }
        }

        return counts.sum()
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}
