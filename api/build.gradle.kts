plugins {
    alias(libs.plugins.micronaut.library)
}

dependencies {
    implementation(project(":domain"))
}
