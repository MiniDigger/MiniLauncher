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
    apply(plugin = "net.minecrell.licenser")
    apply(plugin = "com.github.johnrengelman.shadow")

    license {
        header = rootProject.file("LICENSE")
        filter.include("**/*.java")
        filter.include("**/*.kt")
    }
}