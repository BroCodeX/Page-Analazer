import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    id("java")
    `java-library`
    application
    id("checkstyle")
    id("com.adarshr.test-logger") version "4.0.0"
    id("io.freefair.lombok") version "8.4"
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("info.picocli:picocli:4.7.5")
    implementation("com.puppycrawl.tools:checkstyle:10.15.0")
    implementation("io.javalin:javalin:6.2.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("io.javalin:javalin-rendering:6.1.6")
    implementation("gg.jte:jte:3.1.12")
    implementation("com.h2database:h2:2.2.224")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("io.javalin:javalin-testtools:6.2.0")
    testImplementation("org.assertj:assertj-core:3.26.3")
    // https://mvnrepository.com/artifact/com.konghq/unirest-java-core
    compileOnly("com.konghq:unirest-java-core:4.4.4")


}

tasks.test {
    useJUnitPlatform()
}


tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}

testlogger {
    showFullStackTraces = true
    theme = ThemeType.MOCHA
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
    }
}

application {
    mainClass.set("hexlet.code.App")
}