import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("java")
    id("org.openapi.generator") version "6.6.0"
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "ru.itmo.vk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("dev.mccue:guava-hash:33.4.0")

    implementation("io.swagger.core.v3:swagger-annotations:2.2.8")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("javax.validation:validation-api:2.0.1.Final")

    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.rest-assured:rest-assured:5.5.1")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.mockito:mockito-junit-jupiter:2.23.4")
    testImplementation("org.hamcrest:hamcrest:2.1")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.15.11")
    testImplementation("org.mockito:mockito-inline:5.2.0")

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "-javaagent:${classpath.find { it.name.contains("mockito-core") }?.absolutePath}"
    )
}

sourceSets.main {
    java.srcDirs(
        "src/main/java",
        "${layout.buildDirectory.get()}/generated/src/main/java"
    )
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("$projectDir/src/main/resources/master.yaml") // Default spec (optional)
    outputDir.set("${layout.buildDirectory.get()}/generated")
    library.set("native")

    configOptions.set(
        mapOf(
            "library" to "native",
            "hideGenerationTimestamp" to "true",
            "openApiNullable" to "false",
        )
    )

    globalProperties.set(
        mapOf(
            "modelDocs" to "false"
        )
    )

    skipValidateSpec.set(false)
    logToStderr.set(true)
    verbose.set(false)
}

tasks.register("openApiGenerateSlave", GenerateTask::class) {
    generatorName.set("java")
    inputSpec.set("$projectDir/src/main/resources/slave.json")
    outputDir.set("${layout.buildDirectory.get()}/generated/slave")
    apiPackage.set("ru.itmo.sharding.slave.api")
    modelPackage.set("ru.itmo.sharding.slave.model")
    invokerPackage.set("ru.itmo.sharding.slave.invoker")

    library.set("native")

    configOptions.set(
        mapOf(
            "library" to "native",
            "hideGenerationTimestamp" to "true",
            "openApiNullable" to "false",
        )
    )

    globalProperties.set(
        mapOf(
            "modelDocs" to "false"
        )
    )

    skipValidateSpec.set(false)
    logToStderr.set(true)
    verbose.set(false)
}

tasks.register("openApiGenerateMaster", GenerateTask::class) {
    generatorName.set("java")
    inputSpec.set("$projectDir/src/main/resources/master.json")
    outputDir.set("${layout.buildDirectory.get()}/generated/master")
    apiPackage.set("ru.itmo.sharding.master.api")
    modelPackage.set("ru.itmo.sharding.master.model")
    invokerPackage.set("ru.itmo.sharding.master.invoker")

    library.set("native")

    configOptions.set(
        mapOf(
            "library" to "native",
            "hideGenerationTimestamp" to "true",
            "openApiNullable" to "false",
        )
    )

    globalProperties.set(
        mapOf(
            "modelDocs" to "false"
        )
    )

    skipValidateSpec.set(false)
    logToStderr.set(true)
    verbose.set(false)
}

tasks.register("openApiGenerateAll") {
    dependsOn("openApiGenerateMaster", "openApiGenerateSlave")
}

tasks.compileJava {
    dependsOn("openApiGenerateAll")
}

sourceSets {
    main {
        java {
            srcDirs(
                "src/main/java",
                "${layout.buildDirectory.get()}/generated/master/src/main/java",
                "${layout.buildDirectory.get()}/generated/slave/src/main/java"
            )
        }
    }
}

tasks.jar {
    dependsOn(tasks.openApiGenerate)
    manifest {
        attributes(
            "Main-Class" to "ru.itmo.vk.Main"
        )
    }
}

tasks.shadowJar {
    archiveClassifier.set("all")
    manifest {
        attributes(
            "Main-Class" to "ru.itmo.vk.Main"
        )
    }
}