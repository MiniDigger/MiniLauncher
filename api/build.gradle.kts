plugins {
    `java-library`
}

val commonsIoVersion = "2.6"
val jsonSimpleVersion = "1.1.1"
val ztZipVersion = "1.13"
val kyoriNbtVersion = "1.12-1.0.0-SNAPSHOT"


dependencies {
    api("commons-io:commons-io:$commonsIoVersion")
    api("com.googlecode.json-simple:json-simple:$jsonSimpleVersion")
    api("org.zeroturnaround:zt-zip:$ztZipVersion")
    api("net.kyori:nbt:$kyoriNbtVersion")
}