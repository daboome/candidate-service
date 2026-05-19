plugins {
    alias(libs.plugins.micronaut.application)
}

micronaut {
    version.set("4.7.6")
    runtime.set(io.micronaut.gradle.MicronautRuntime.NETTY)
    processing {
        incremental.set(true)
    }
}

application {
    mainClass.set("com.cfa.candidate.application.Application")
}

dependencies {
    implementation(platform("io.micronaut.platform:micronaut-platform:4.7.6"))
    implementation(project(":domain"))
    runtimeOnly(project(":infrastructure"))
    runtimeOnly(project(":api"))

    annotationProcessor("io.micronaut:micronaut-inject-java")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.liquibase:micronaut-liquibase")
    implementation("io.micronaut.openapi:micronaut-openapi")
    implementation("jakarta.inject:jakarta.inject-api")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly(libs.logstash.logback.encoder)

    testImplementation(libs.testcontainers.postgres)
    testImplementation(libs.testcontainers.junit)
    testImplementation("org.testcontainers:testcontainers:${libs.versions.testcontainers.get()}")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.micronaut:micronaut-http-client")
    testRuntimeOnly(project(":api"))
}
