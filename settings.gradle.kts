rootProject.name = "mp-reactive-chat"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyVersion: String by settings
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.namespace) {
                "org.jetbrains.kotlin" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin" -> useVersion(kotlinVersion)
                "org.springframework" -> useVersion(springBootVersion)
                "io.spring" -> useVersion(springDependencyVersion)
            }
        }
    }
}

