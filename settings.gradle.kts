pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.diffplug.spotless") version "6.25.0" apply false
}

rootProject.name = "candidate-service"

include("domain")
include("infrastructure")
include("application")
include("api")
