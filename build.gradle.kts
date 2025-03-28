plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.itmo.vk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("io.grpc:grpc-netty-shaded:1.58.0")
    implementation("io.grpc:grpc-protobuf:1.58.0")
    implementation("io.grpc:grpc-stub:1.58.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.protobuf:protobuf-java:3.24.4")

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

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.4"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.58.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                if (!task.builtins.any { it.name == "java" }) {
                    create("java")
                }
            }
            task.plugins {
                create("grpc") // Генерация gRPC-кода
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

application {
    mainClass.set("ru.itmo.vk.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.itmo.vk.Main"
    }
}