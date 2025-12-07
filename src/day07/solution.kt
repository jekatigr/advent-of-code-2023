package day07

import utils.runDaySolutions

val CARDS_WEIGHT = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'T' to 10,
    '9' to 9,
    '8' to 8,
    '7' to 7,
    '6' to 6,
    '5' to 5,
    '4' to 4,
    '3' to 3,
    '2' to 2,
)
val SIMPLE_CARDS_WEIGHT = CARDS_WEIGHT + mapOf(
    'J' to 11,
)
val JOCKER_CARDS_WEIGHT = CARDS_WEIGHT + mapOf(
    'J' to 1,
)

enum class COMBINATIONS(val weight: Int) {
    FIVE(7),
    FOUR(6),
    FULL_HOUSE(5),
    THREE(4),
    TWO_PAIRS(3),
    ONE_PAIR(2),
    HIGH_CARD(1),
}

abstract class Card(val char: Char): Comparable<Card>

class SimpleCard(char: Char): Card(char) {
    override fun compareTo(other: Card): Int {
        return SIMPLE_CARDS_WEIGHT[char]!! - SIMPLE_CARDS_WEIGHT[other.char]!!
    }
}
class WithJockerCard(char: Char): Card(char) {
    override fun compareTo(other: Card): Int {
        return JOCKER_CARDS_WEIGHT[char]!! - JOCKER_CARDS_WEIGHT[other.char]!!
    }
}

abstract class Hand(val cards: Array<Card>, val bid: Int): Comparable<Hand> {
    abstract fun getWeight(): Int

    override fun compareTo(other: Hand): Int {
        val result = getWeight() - other.getWeight()

        if (result != 0) {
            return result
        }

        return this.compareEqualTypes(other)
    }

    fun getCombination(counts: Map<Char, Int>): COMBINATIONS {
        if (counts.any { it.value == 5 }) {
            return COMBINATIONS.FIVE
        }

        if (counts.any { it.value == 4 }) {
            return COMBINATIONS.FOUR
        }

        if (counts.any { it.value == 3 } && counts.any { it.value == 2 }) {
            return COMBINATIONS.FULL_HOUSE
        }

        if (counts.any { it.value == 3 }) {
            return COMBINATIONS.THREE
        }

        var pairs = 0
        for (count in counts) {
            if (count.value == 2) {
                pairs += 1
            }
        }

        if (pairs == 2) {
            return COMBINATIONS.TWO_PAIRS
        }

        if (pairs == 1) {
            return COMBINATIONS.ONE_PAIR
        }

        return COMBINATIONS.HIGH_CARD
    }

    fun compareEqualTypes(other: Hand): Int {
        for (i in this.cards.indices) {
            if (cards[i].compareTo(other.cards[i]) == 0) {
                continue
            }

            return if (cards[i] > other.cards[i]) {
                1
            } else {
                -1
            }
        }

        return 0
    }
}

class SimpleHand(cards: Array<Card>, bid: Int): Hand(cards, bid) {
    override fun getWeight(): Int {
        val counts = cards.groupingBy { it.char }.eachCount()

        return getCombination(counts).weight
    }
}

class WithJockerHand(cards: Array<Card>, bid: Int): Hand(cards, bid) {
    override fun getWeight(): Int {
        val counts = cards.groupingBy { it.char }.eachCount().toMutableMap()
        val jockers = counts['J'] ?: 0

        if (jockers == 0) {
            return getCombination(counts).weight
        }

        counts['J'] = 0

        val combination = getCombination(counts)

        return when(combination) {
            COMBINATIONS.HIGH_CARD -> {
                when(jockers) {
                    1 -> COMBINATIONS.ONE_PAIR
                    2 -> COMBINATIONS.THREE
                    3 -> COMBINATIONS.FOUR
                    4, 5 -> COMBINATIONS.FIVE
                    else -> {throw Error("Impossible jockers")}
                }.weight
            }
            COMBINATIONS.ONE_PAIR -> {
                when(jockers) {
                    1 -> COMBINATIONS.THREE
                    2 -> COMBINATIONS.FOUR
                    3 -> COMBINATIONS.FIVE
                    else -> {throw Error("Impossible jockers")}
                }.weight
            }
            COMBINATIONS.TWO_PAIRS -> {
                when(jockers) {
                    1 -> COMBINATIONS.FULL_HOUSE
                    else -> {throw Error("Impossible jockers")}
                }.weight
            }
            COMBINATIONS.THREE -> {
                when(jockers) {
                    1 -> COMBINATIONS.FOUR
                    2 -> COMBINATIONS.FIVE
                    else -> {throw Error("Impossible jockers")}
                }.weight
            }
            COMBINATIONS.FOUR -> {
                when(jockers) {
                    1 -> COMBINATIONS.FIVE
                    else -> {throw Error("Impossible jockers")}
                }.weight
            }

            else -> {throw Error("Impossible hand")}
        }
    }
}

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun part1(input: List<String>): Int {
        val hands = input.map { line ->
            val arr = line.split(" ")

            SimpleHand(arr[0].map { SimpleCard(it) }.toTypedArray(), arr[1].toInt())
        }

        val sortedHands = hands.sorted()

        return sortedHands.foldIndexed(0) { index, acc, hand -> (index + 1) * hand.bid + acc }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { line ->
            val arr = line.split(" ")

            WithJockerHand(arr[0].map { WithJockerCard(it) }.toTypedArray(), arr[1].toInt())
        }

        val sortedHands = hands.sorted()

        return sortedHands.foldIndexed(0) { index, acc, hand -> (index + 1) * hand.bid + acc }
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}
