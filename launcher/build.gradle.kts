val log4j2Version = "2.11.1"

dependencies {
    implementation(project(":api"))
    implementation("org.apache.logging.log4j:log4j-core:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j2Version")
}