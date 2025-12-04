package day01

import utils.Skip
import utils.runDaySolutions



fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun part1(input: List<String>): Int {
        var result = 0;

        for (line in input) {
            val chars = line.mapNotNull { it.toString().toIntOrNull() }

            result += chars.first() * 10 + chars.last()
        }

        return result
    }

    val numbers = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )

    fun part2(input: List<String>): Int {
        var result = 0

        for (line in input) {
            val chars = mutableListOf<Int>()
            var index = 0

            while (index < line.length) {
                val num = line[index].toString().toIntOrNull()
                if (num != null) {
                    chars.add(num)

                    index += 1

                    continue
                }

                val substr = line.substring(index)

                for (number in numbers.keys) {
                    if (substr.startsWith(number)) {
                        chars.add(numbers[number] ?: -1)

                        break
                    }
                }

                index += 1
            }

            result += chars.first() * 10 + chars.last()
        }

        return result
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}
