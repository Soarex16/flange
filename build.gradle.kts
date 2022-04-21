import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
    antlr
}

group = "com.soarex"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(kotlin("test"))

    antlr("org.antlr:antlr4:4.9.3")
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.graalvm.js:js:22.0.0")
    implementation("org.graalvm.js:js-scriptengine:22.0.0")
}

application {
    mainClass.set("com.soarex.flange.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    dependsOn("generateGrammarSource")
    kotlinOptions.jvmTarget = "1.8"
}

tasks.generateGrammarSource {
    val grammarOutputDir = "generated-src/antlr/main/com/soarex/flange/parser"
    outputDirectory = File("${project.buildDir}/$grammarOutputDir")
    arguments.plusAssign(listOf("-no-listener", "-visitor"))
}