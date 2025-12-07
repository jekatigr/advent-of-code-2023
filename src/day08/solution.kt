package day08

import utils.runDaySolutions


class Graph(val instructions: String, val graph: Map<String, Pair<String, String>>) {
    fun stepsBetween(from: String, to: String): Int {
        var current = from
        var steps = 0

        while (current != to) {
            val instruction = instructions[steps % instructions.length]

            current = if (instruction == 'L') {
                graph[current]!!.first
            } else {
                graph[current]!!.second
            }

            steps += 1
        }

        return steps
    }

    private val cache = mutableMapOf<String, Pair<Long, String>>()

    fun goUtilNextZ(from: String, startInstruction: Long): Pair<Long, String> {
        if (cache.containsKey(from)) {
            return cache[from]!!
        }

        var current = from
        var steps = 0L

        do {
            val instruction = instructions[((steps + startInstruction) % instructions.length).toInt()]

            current = if (instruction == 'L') {
                graph[current]!!.first
            } else {
                graph[current]!!.second
            }

            steps += 1
        } while (!current.endsWith('Z'))

        cache[from] = Pair(steps, current)

        return cache[from]!!
    }

    fun stepsUtilZForAllEntries(entries: List<String>): Long {
        val steps = Array(entries.size) { 0L }
        val currents = entries.toMutableList()

        do {
            val lowest = getIndexForLowest(steps)

            val res = goUtilNextZ(currents[lowest], steps[lowest])

            steps[lowest] += res.first
            currents[lowest] = res.second


        } while (!allEqual(steps))

        return steps[0]
    }

    private fun allEqual(steps: Array<Long>): Boolean {
        for (step in steps) {
            if (step != steps[0]) return false
        }

        return true
    }

    private fun getIndexForLowest(steps: Array<Long>): Int {
        var minIndex = 0

        for ((i, v) in steps.withIndex()) {
            if (v < steps[minIndex]) {
                minIndex = i
            }
        }

        return minIndex
    }

    companion object {
        fun from(instructions: String, connections: List<String>): Graph {
            val graph = mutableMapOf<String, Pair<String, String>>()

            for (line in connections) {
                val arr = line.split(" = ")
                val targets = arr[1].removePrefix("(").removeSuffix(")").split(", ")
                graph[arr[0]] = Pair(targets[0], targets[1])
            }

            return Graph(instructions, graph)
        }
    }
}

fun main() {
    val day = (object {}).javaClass.packageName.takeLast(2).toInt()

    fun part1(input: List<String>): Int {
        val graph = Graph.from(input[0], input.takeLast(input.size -2))

        return graph.stepsBetween("AAA", "ZZZ")
    }

    fun part2(input: List<String>): Long {
        val graph = Graph.from(input[0], input.takeLast(input.size -2))

        val entries = graph.graph.keys.filter { it.endsWith('A') }

        return graph.stepsUtilZForAllEntries(entries)
    }

    runDaySolutions(day, ::part1, ::part2,  setOf())
}
