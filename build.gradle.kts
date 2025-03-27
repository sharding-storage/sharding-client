plugins {
    id("java")
    //id("com.google.protobuf") version "0.9.4"
    id("org.openapi.generator") version "6.6.0"
    //id("org.springframework.boot") version "3.2.0"
    //id("io.spring.dependency-management") version "1.1.4"
}

group = "ru.itmo.vk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("io.grpc:grpc-netty-shaded:1.58.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("dev.mccue:guava-hash:33.4.0")

    implementation("io.swagger.core.v3:swagger-annotations:2.2.8")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("javax.validation:validation-api:2.0.1.Final")
    //implementation("org.springframework.boot:spring-boot-starter-web")
    //testImplementation("org.springframework.boot:spring-boot-starter-test")

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


openApiGenerate {
    generatorName.set("java")
    inputSpec.set("$projectDir/src/main/resources/openapi.yaml")
    outputDir.set("$buildDir/generated/nodes")
    apiPackage.set("ru.itmo.vk..sharding.api")
    modelPackage.set("ru.itmo.vk..sharding.model")
    invokerPackage.set("ru.itmo.vk..sharding.invoker")

    configOptions.set(
        mapOf(
            "java8" to "true",
            "dateLibrary" to "java8-localdatetime",
            "library" to "rest-assured",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "useSpringBoot3" to "true",
            "reactive" to "false",
            "serializationLibrary" to "jackson",
            "useBeanValidation" to "true",
            "openApiNullable" to "false"
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

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

