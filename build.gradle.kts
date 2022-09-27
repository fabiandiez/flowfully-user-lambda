import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.transformers.*
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.springframework.boot.experimental.thin-launcher") version "1.0.28.RELEASE"

    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "tech.fdiez"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val springBootVersion = "2.7.4"
val springCloudVersion = "3.2.7"


dependencies {
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.amazonaws:aws-lambda-java-events:3.11.0")

    implementation("org.springframework.cloud:spring-cloud-function-web:${springCloudVersion}")
    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws:${springCloudVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-webflux:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${springBootVersion}")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.7")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.0")

    implementation("com.bol:spring-data-mongodb-encrypt:2.8.0")

    // TODO: Remove this dependency once spring fixes this
    implementation("org.yaml:snakeyaml:1.32")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")

    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Start-Class"] = "tech.fdiez.flowfullybackend.FlowfullyBackendApplication"
    }
}

tasks.assemble {
    dependsOn("shadowJar", "thinJar")
}


tasks.withType<ShadowJar> {
    archiveFileName.set("flowfullyBackendApplication.jar")

    dependencies {
        exclude("org.springframework.cloud:spring-cloud-function-web")
    }
    // Required for Spring
    mergeServiceFiles()
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
    transform(PropertiesFileTransformer::class.java) {
        paths.add("META-INF/spring.factories")
        mergeStrategy = "append"
    }
}