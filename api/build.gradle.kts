plugins {
    alias(libs.plugins.micronaut.library)
}

micronaut {
    version.set("4.7.6")
    processing {
        incremental.set(true)
    }
}

tasks.named<JavaCompile>("compileJava") {
    options.compilerArgs.add("-Amicronaut.openapi.views.spec=swagger-ui.enabled=true")
}

dependencies {
    implementation(platform("io.micronaut.platform:micronaut-platform:4.7.6"))
    implementation(project(":application"))
    implementation(project(":domain"))

    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")

    implementation("io.micronaut:micronaut-http-server")
    implementation("io.projectreactor:reactor-core")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.openapi:micronaut-openapi-annotations")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("jakarta.validation:jakarta.validation-api")
}
