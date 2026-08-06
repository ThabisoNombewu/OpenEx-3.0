pluginManagement {
    repositories {
        gradlePluginPortal()  // This is where Spring Boot plugin lives
        maven {
            url = uri("https://repo.spring.io/release")
        }

    }
}

rootProject.name = "openex-backend"