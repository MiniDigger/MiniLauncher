import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    kotlin("jvm") version "1.2.61"
    id("net.minecrell.licenser") version "0.3"
    id("com.github.johnrengelman.shadow") version "2.0.4"
}

allprojects {
    group = "me.minidigger"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "java")
    apply(plugin = "net.minecrell.licenser")
    apply(plugin = "com.github.johnrengelman.shadow")

    java.sourceCompatibility = VERSION_1_8
    java.targetCompatibility = VERSION_1_8

    val compileJava by tasks.getting(JavaCompile::class) {
        options.compilerArgs.add("-proc:none")
    }

    license {
        header = rootProject.file("LICENSE")
        filter.include("**/*.java")
        filter.include("**/*.kt")
        filter.exclude("**/jmccc/**")
    }
}