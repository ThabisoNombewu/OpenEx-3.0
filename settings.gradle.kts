pluginManagement {
    repositories {
        gradlePluginPortal()  // This is where Spring Boot plugin lives
        maven {
            url = uri("https://repo.spring.io/release")
        }
        maven {
            url = uri("http://repo.spring.io/release")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
        maven {
            url = uri("http://repo.maven.apache.org/maven2/")
            isAllowInsecureProtocol = true
        }
    }
}

rootProject.name = "openex-backend"