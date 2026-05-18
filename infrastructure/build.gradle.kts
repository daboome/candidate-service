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
    implementation(project(":domain"))

    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.liquibase:micronaut-liquibase")
    implementation("org.postgresql:postgresql")
}
