package com.soarex.flange

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText

class ParserTestArgumentsProvider : ArgumentsProvider {
    val baseDir = "src/test/resources/testData/parser"

    val sourceExtension = "fl"
    val resultExtension = "txt"

    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        val sourceFiles = getFilesByExtension(baseDir, sourceExtension)
        val testResultFiles = getFilesByExtension(baseDir, resultExtension)

        if (sourceFiles.size != testResultFiles.size)
            throw RuntimeException("Inconsistent test data. Source files count: ${sourceFiles.size}, test result files count: ${testResultFiles.size}")

        if (!fileNamesAreEquals(sourceFiles, testResultFiles))
            throw RuntimeException("Inconsistent test data. Not all source files matched with test results")

        return sourceFiles
            .zip(testResultFiles)
            .map { Arguments.of(it.first.readText(), it.second.readText()) }
            .stream()
    }

    private fun getFilesByExtension(base: String, extension: String): List<Path> = Files
        .walk(Path.of(base))
        .filter { it.extension == extension }
        .sorted()
        .toList()

    private fun fileNamesAreEquals(first: List<Path>, second: List<Path>) =
        first.map { it.nameWithoutExtension } == second.map { it.nameWithoutExtension }
}