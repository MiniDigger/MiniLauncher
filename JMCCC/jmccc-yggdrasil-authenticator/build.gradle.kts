val junitVersion = "4.12"

dependencies {
    implementation(project(":JMCCC/jmccc"))

    testImplementation("junit:junit:$junitVersion")
}
