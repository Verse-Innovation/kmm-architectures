plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":libraries:soa"))
        }
    }
}

android {
    namespace = "io.verse.architectures.soa.test"
}