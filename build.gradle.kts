import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import java.lang.System.getProperty

plugins {
    kotlin("jvm") version("2.0.20")
    id("org.graalvm.buildtools.native") version("0.10.3")
}

val hexagonVersion = "4.0.0-A3"
val flywayVersion = "10.18.1"
val postgresqlVersion = "42.7.4"
val kafkaVersion = "3.8.0"
val gradleScripts = "https://raw.githubusercontent.com/hexagontk/hexagon/$hexagonVersion/gradle"

ext.set("options", "-Xmx48m")
ext.set("modules", "java.logging")
ext.set("applicationClass", "org.example.ApplicationKt")

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/application.gradle")
apply(from = "$gradleScripts/native.gradle")

defaultTasks("build")

version="1.0.0"
group="org.example"
description="Service's description"

dependencies {
    "implementation"("com.hexagontk:http_server_netty:$hexagonVersion")
    "implementation"("com.hexagontk:serialization_jackson_json:$hexagonVersion")
//    "implementation"("org.flywaydb:flyway-core:$flywayVersion")
//    "implementation"("org.postgresql:postgresql:$postgresqlVersion")
//    "implementation"("org.apache.kafka:kafka-clients:$kafkaVersion")

    "testImplementation"("com.hexagontk:http_client_jetty:$hexagonVersion")
}

extensions.configure<GraalVMExtension> {
    fun option(name: String, value: (String) -> String): String? =
        getProperty(name)?.let(value)

    binaries {
        named("main") {
            listOfNotNull(
                "--static", // Won't work on Windows or macOS
                "-R:MaxHeapSize=16",
                option("enableMonitoring") { "--enable-monitoring" },
            )
            .forEach(buildArgs::add)
        }
    }
}

tasks.wrapper {
    gradleVersion = "8.10.2"
    distributionType = ALL
}
