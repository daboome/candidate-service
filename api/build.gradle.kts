plugins {
    alias(libs.plugins.micronaut.library)
}

micronaut {
    version.set("4.7.6")
    processing {
        incremental.set(true)
    }
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation("io.micronaut:micronaut-http-server")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("jakarta.validation:jakarta.validation-api")
}
