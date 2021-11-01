
val kotlinVersion: String by project
val javaVersion: String by project
val ktorVersion: String by project

val kotlinRSocketVersion: String by project
val kotlinDateTimeVersion: String by project
val kotlinJsonVersion: String by project
val coroutinesCoreVersion: String by project
val coroutinesTestVersion: String by project
val turbineVersion: String by project

val commonUuidVersion: String by project
val kotlinHtmlVersion: String by project

val styledVersion: String by project
val kotlinWrapperVersion: String by project
val reactVersion: String by project
val reactRouterVersion: String by project
val antDVersion: String by project
val kotlinAntDVersion: String by project

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("plugin.spring") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
}

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

kotlin {
    jvm {
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.springframework.boot")
        apply(plugin = "org.jetbrains.kotlin.plugin.spring")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        tasks {
            withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
                kotlinOptions {
                    freeCompilerArgs = listOf("-Xjsr305=strict")
                    jvmTarget = JavaVersion.valueOf(javaVersion).majorVersion
                }
            }
            withType<Test> {
                useJUnitPlatform()
            }
        }
        withJava()
    }
    js(compiler = org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType.IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
                outputFileName = "main.js"
                outputPath = File(buildDir, "processedResources/jvm/main/static")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.benasher44:uuid:$commonUuidVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinHtmlVersion")
                api("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDateTimeVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinJsonVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCoreVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")
                api("app.cash.turbine:turbine:$turbineVersion")
            }
        }
        val jsCommonMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.rsocket.kotlin:rsocket-core:$kotlinRSocketVersion")
                implementation("io.rsocket.kotlin:rsocket-transport-ktor-client:$kotlinRSocketVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
            }
        }
        val jsMain by getting {
            dependsOn(commonMain)
            dependsOn(jsCommonMain)
            dependencies {
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))
                implementation(npm("react-is", reactVersion))
                implementation(npm("styled-components", styledVersion))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion$kotlinWrapperVersion$kotlinVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion$kotlinWrapperVersion$kotlinVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:$reactRouterVersion$kotlinWrapperVersion$kotlinVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:$styledVersion$kotlinWrapperVersion$kotlinVersion")
                implementation("io.sunland:kotlin-antd:$antDVersion$kotlinAntDVersion$kotlinVersion")
            }
        }
        val jsTest by getting
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-actuator")
                implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
                implementation("org.springframework.boot:spring-boot-starter-rsocket")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
                implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
            }
        }
        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
                implementation("io.projectreactor:reactor-test")
            }
        }
    }

    tasks {
        getByName<Copy>("jvmProcessResources") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            dependsOn(getByName("jsBrowserDevelopmentWebpack"))
        }
    }
}
