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
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    runtimeOnly(project(":api"))

    runtimeOnly("ch.qos.logback:logback-classic")
}
