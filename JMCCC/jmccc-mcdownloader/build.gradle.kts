val xzVersion = "1.5"
val httpasyncclientVersion = "4.1.2"
val ehcacheVersion = "3.1.1"
val cacheapiVersion = "1.0.0"

dependencies {
    implementation("org.to2mbn:jmccc")
    implementation("org.tukaani:xz:$xzVersion")
    implementation("org.apache.httpcomponents:httpasyncclient$httpasyncclientVersion")
    implementation("org.ehcache.modules:ehcache-impl:$ehcacheVersion")
    implementation("javax.cache:cache-api:$cacheapiVersion")
}
