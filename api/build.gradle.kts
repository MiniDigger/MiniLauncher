plugins {
    `java-library`
}

val commonsIoVersion = "2.6"
val jsonSimpleVersion = "1.1.1"
val ztZipVersion = "1.13"
val kyoriNbtVersion = "1.12-1.0.0-SNAPSHOT"
val checkerQualVersion = "2.5.4"
val slf4jVersion = "1.8.0-beta2"
val moshiVersion = "1.6.0"
val okHttpVersion = "3.11.0"
val asmVersion = "6.2.1"

dependencies {
    api("commons-io:commons-io:$commonsIoVersion")
    api("com.googlecode.json-simple:json-simple:$jsonSimpleVersion")
    api("org.zeroturnaround:zt-zip:$ztZipVersion")
    api("net.kyori:nbt:$kyoriNbtVersion")
    api("org.checkerframework:checker-qual:$checkerQualVersion")
    api("org.slf4j:slf4j-api:$slf4jVersion")
    api("com.squareup.moshi:moshi:$moshiVersion")
    api("com.squareup.okhttp3:okhttp:$okHttpVersion")
    api("org.ow2.asm:asm:$asmVersion")
    api("org.ow2.asm:asm-util:$asmVersion")
    api("org.ow2.asm:asm-commons:$asmVersion")
}