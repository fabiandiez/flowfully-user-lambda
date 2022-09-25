import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.transformers.*
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	id("com.github.johnrengelman.shadow") version "7.1.2"

	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "tech.fdiez"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
	implementation("com.amazonaws:aws-lambda-java-events:3.11.0")

	implementation("org.springframework.cloud:spring-cloud-function-web:3.2.7")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.7.4")
	implementation("org.springframework.cloud:spring-cloud-function-adapter-aws:3.2.7")
	implementation("org.springframework.boot:spring-boot-starter-webflux:2.7.3")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")

	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")

	testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
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
	dependsOn("shadowJar")
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