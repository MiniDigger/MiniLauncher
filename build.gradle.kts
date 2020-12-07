import org.gradle.api.JavaVersion.VERSION_11
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    kotlin("jvm") version "1.2.61"
    id("net.minecrell.licenser") version "0.3"
    id("com.github.johnrengelman.shadow") version "2.0.4"
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("application")
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
    apply(plugin = "application")
    apply(plugin = "net.minecrell.licenser")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.openjfx.javafxplugin")

    java.sourceCompatibility = VERSION_11
    java.targetCompatibility = VERSION_11

    val compileJava by tasks.getting(JavaCompile::class) {
        options.compilerArgs.add("-proc:none")
        options.encoding = "UTF-8"
    }

    javafx {
        version = "13"
        modules("javafx.controls", "javafx.fxml", "javafx.web")
    }

    license {
        header = rootProject.file("LICENSE")
        filter.include("**/*.java")
        filter.include("**/*.kt")
        filter.exclude("**/jmccc/**")
    }
}
