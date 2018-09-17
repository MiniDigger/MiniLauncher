val log4j2Version = "2.11.0"
val jmcccVersion = "2.5-SNAPSHOT"

dependencies {
    //implementation(project(":api"))
    implementation(project(":renderer"))
    implementation("org.apache.logging.log4j:log4j-core:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j2Version")
    implementation("org.to2mbn:jmccc:$jmcccVersion")
    implementation("org.to2mbn:jmccc-yggdrasil-authenticator:$jmcccVersion")
    implementation("org.to2mbn:jmccc-mcdownloader:$jmcccVersion")
    implementation("org.to2mbn:jmccc-mojang-api:$jmcccVersion")
}