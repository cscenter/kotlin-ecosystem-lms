val kotlinVersion: String = "1.9.10"
val logbackVersion: String = "1.4.11"
val datetimeVersion: String = "0.4.1"

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("io.ktor.plugin") version "2.3.4"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
}

group = "org.jetbrains.edu"
version = "0.0.2"

application {
    mainClass.set("MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-default-headers-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-resources")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.flywaydb:flyway-core:9.16.0")
    implementation(enforcedPlatform("org.jetbrains.exposed:exposed-bom:0.44.0"))
    implementation("org.jetbrains.exposed:exposed-core")
    implementation("org.jetbrains.exposed:exposed-dao")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime")

    implementation(project(":shared"))

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.4")
}

detekt {
    ignoreFailures = true
    buildUponDefaultConfig = false
}

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("customDetekt") {
    description = "Runs detekt"
    setSource(files("src/main/kotlin", "src/test/kotlin"))
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$projectDir/config/detekt.yml"))
    debug = true
    ignoreFailures = false
    reports {
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
    include("**/*.kt")
    include("**/*.kts")
    exclude("resources/")
    exclude("build/")
}

val fatJar = task("fatJar", type = org.gradle.jvm.tasks.Jar::class) {
    dependsOn(tasks.get("distZip"))

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Implementation-Title"] = "Social Connections Fat Jar"
        attributes["Implementation-Version"] = project.version
        attributes["Main-Class"] = "io.ktor.server.netty.EngineMain"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks.get("distTar").dependsOn(fatJar)

tasks["build"].apply {
    dependsOn(fatJar)
    doLast {
        delete("$rootDir/build")
        copy {
            from(fatJar)
            into(file("$rootDir/build"))
        }
    }
}

tasks {
    jib {
        from {
            image = "maven:3.9.0-eclipse-temurin-17"

            platforms {
                platform {
                    architecture = if (System.getProperty("os.arch").equals("aarch64")) "arm64" else "amd64"
                    os = "linux"
                }
            }
        }
    }
}
