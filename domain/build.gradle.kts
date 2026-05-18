plugins {
    `java-library`
    jacoco
}

// Pure domain — no Micronaut, JPA, or infrastructure dependencies.
dependencies {}

val jacocoCoverageExcludes =
    listOf(
        "com/cfa/candidate/domain/port/**",
        "com/cfa/candidate/domain/exception/**",
        "com/cfa/candidate/domain/model/EligibilityAuditEntry.class",
        "com/cfa/candidate/domain/model/PageResult.class",
        "com/cfa/candidate/domain/model/ExamPass.class",
        "com/cfa/candidate/domain/model/Education.class",
        "com/cfa/candidate/domain/model/*Level.class",
        "com/cfa/candidate/domain/model/EligibilityStatus.class",
        "com/cfa/candidate/domain/model/ProgramLevel.class",
        "com/cfa/candidate/domain/service/EligibilityResult.class")

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(
        files(
            classDirectories.files.map { dir ->
                fileTree(dir) { exclude(jacocoCoverageExcludes) }
            }))
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    classDirectories.setFrom(tasks.jacocoTestReport.get().classDirectories)
    violationRules {
        rule {
            limit {
                counter = "LINE"
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
