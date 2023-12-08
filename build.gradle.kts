import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import java.lang.System.getProperty

plugins {
    kotlin("jvm") version("1.9.21")
    id("org.graalvm.buildtools.native") version("0.9.28")
}

val hexagonVersion = "3.4.5"
val gradleScripts = "https://raw.githubusercontent.com/hexagonkt/hexagon/$hexagonVersion/gradle"

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
    "implementation"("com.hexagonkt:http_server_jetty:$hexagonVersion")
    "implementation"("com.hexagonkt:serialization_jackson_json:$hexagonVersion")
    "implementation"("org.slf4j:slf4j-nop:2.0.9")

    "testImplementation"("com.hexagonkt:http_client_jetty:$hexagonVersion")
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
