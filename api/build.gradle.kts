plugins {
    `java-library`
}

val commonsIoVersion = "2.6"
val jsonSimpleVersion = "1.1.1"
val ztZipVersion = "1.13"
val moonLakeNbtVersion = "v1.0.1-Release"


dependencies {
    api("commons-io:commons-io:$commonsIoVersion")
    api("com.googlecode.json-simple:json-simple:$jsonSimpleVersion")
    api("org.zeroturnaround:zt-zip:$ztZipVersion")
    api("com.github.McMoonLakeDev:MoonLakeNBT:$moonLakeNbtVersion")
}