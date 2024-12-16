plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.tagd.arch)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(project(":libraries:soa:soa-test"))
            api(libs.tagd.arch.test)
        }
        androidMain.dependencies {
            api(libs.gson)
            api(libs.tagd.android)
        }

        val androidUnitTest by getting
        androidUnitTest.dependencies {
            implementation(libs.tagd.android)
        }
    }
}

android {
    namespace = "io.verse.architectures.soa"
}

pomBuilder {
    description.set("Soa core library")
}