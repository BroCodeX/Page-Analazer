import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    id("java")
    `java-library`
    application
    id("checkstyle")
    id("com.adarshr.test-logger") version "4.0.0"
    id("io.freefair.lombok") version "8.4"
    jacoco
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