import java.io.File
import java.math.BigInteger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest

fun main() {
    getInput(12)
}

fun prepareRequest(inputUrl: String): HttpRequest.Builder {
    return HttpRequest.newBuilder()
        .uri(URI.create(inputUrl))
        .header("cookie", "session=$SESSION")
}

fun send(request: HttpRequest): HttpResponse<String> {
    return HttpClient.newBuilder().build()
        .send(request, HttpResponse.BodyHandlers.ofString())
}

fun treatPart(part: Int, answer: Number, day: Int) {
    print("Submit part $part result $answer? (y|n): ")
    if (readLine() == "y") {
        sendAnswer(day, part, answer)
    }
}

fun getInput(day: Int) {
    val inputUrl = "https://adventofcode.com/$YEAR/day/$day/input"

    val request = prepareRequest(inputUrl)
        .GET()
        .build()

    val response = send(request)
    val path = Paths.get("src", String.format("Day%02d.txt", day))
    Files.write(path, response.body().toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
}

fun sendAnswer(day: Int, level: Int, answer: Number) {
    val answerUrl = "https://adventofcode.com/$YEAR/day/$day/answer"

    val request = prepareRequest(answerUrl)
        .POST(HttpRequest.BodyPublishers.ofString("level=$level,answer=$answer"))
        .build();

    val response = send(request);
    println(response.body())
}

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> {
    return File("src", "$name.txt").readLines()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

abstract class AoC(val day: Int) {

    fun execute() {
        // test if implementation meets criteria from the description for part 1, like:
        val testInput = readInput(String.format("Day%02d_test", day))
        check(part1(testInput) == check1ExpectedResult())

        // test was ok retrieve the real data
        val input = readInput(String.format("Day%02d", day))

        // get the answer with the real data for part 1
        treatPart(1, part1(input), day)

        // test if implementation meets criteria from the description for part 2, like:
        check(part2(testInput) == check2ExpectedResult())

        // get the answer with the real data for part 2
        treatPart(2, part2(input), day)
    }

    abstract fun part1(input: List<String>): Number
    abstract fun check1ExpectedResult(): Number
    abstract fun part2(input: List<String>): Number
    abstract fun check2ExpectedResult(): Number
}
