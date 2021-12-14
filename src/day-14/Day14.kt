fun main() {
    fun parseInput(input: List<String>): Pair<String, Map<String, String>> {
        val source = input.first()
        val replacements = input
            .filter { it.contains("->") }
            .map { it.split(" -> ") }
            .associate { (a, b) -> a to b }
        return Pair(source, replacements)
    }

    fun part1(input: List<String>, iterations: Int): Int {
        val (source, replacements) = parseInput(input)
        val freqs = (0 until iterations)
            .fold(source) { it, _ ->
                it
                    .windowed(2, partialWindows = true) {
                        if (it.length == 2)
                            "${it[0]}${replacements[it.toString()]}"
                        else it
                    }
                    .joinToString("")
            }
            .groupingBy { it }
            .eachCount()
            .values
        val max = freqs.maxOf { it }
        val min = freqs.minOf { it }
        return max - min
    }

    fun countPairs(source: String): Map<String, Long> {
        return source
            .windowed(2) { it.toString() }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
    }

    fun part2(input: List<String>, iterations: Int): Long {
        val (source, replacements) = parseInput(input)
        val freqs = (0 until iterations)
            .fold(countPairs(source)) { map, _ ->
                map
                    .flatMap { (symbolPair, count) ->
                        val newChar = replacements[symbolPair]!!
                        val fst = symbolPair[0] + newChar
                        val snd = newChar + symbolPair[1]
                        listOf(fst to count, snd to count)
                    }
                    .groupBy { it.first }
                    .mapValues { (_, counts) -> counts.sumOf { it.second } }
            }
            .flatMap { (pair, count) -> pair.map { it to count } }
            .groupBy { (char, _) -> char }
            .mapValues { (_, listCounts) -> listCounts.sumOf { (_, count) -> count } }
            .mapValues { (char, count) ->
                if (char == source.first() || char == source.last()) (count + 1) / 2
                else count / 2
            }
            .values
        val max = freqs.maxOf { it }
        val min = freqs.minOf { it }
        return max - min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput, 10) == 1588)
    println(part1(testInput, 10))
    println(part2(testInput, 10))

    val input = readInput("Day14")
    println(part1(input, 10))
    println(part2(input, 10))

    println(part2(input, 40))
}

