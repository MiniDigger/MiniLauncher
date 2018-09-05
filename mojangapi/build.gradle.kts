val junitVersion = "4.12"
val moshiVersion = "1.6.0"
val okhttpVersion = "3.11.0"

dependencies {
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

    testImplementation("junit:junit:$junitVersion")
}
