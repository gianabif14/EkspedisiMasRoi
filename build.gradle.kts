plugins {
    id("java")
    id("application")
}

group = "com.github.orions29.ekspedisi"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

//    Buat .env files yang nanti dibutuhkan
    // Source: https://mvnrepository.com/artifact/io.github.cdimascio/dotenv-java
    implementation("io.github.cdimascio:dotenv-java:3.2.0")

// Logging sama SLF4J
    // Source: https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:2.0.18")
    // Source: https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.32")

// Database
    // Source: https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.8")

// QR Code Generator (ZXing)
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

// Webcam Capture
    implementation("com.github.sarxos:webcam-capture:0.3.12")
    implementation("com.github.sarxos:webcam-capture-driver-openimaj:0.3.12")
}


//Mengatur Dist task agar file .env otomatis masuk ke folder distribusi bawaan
distributions {
    main {
        contents {
            from(layout.projectDirectory) {
                include(".env", "/assets/**/*")
                // Memasukkannya ke dalam folder bin agar sejajar dengan script .bat
                into("bin")
            }
        }
    }
}

// Task Buat Build Java Version 26
tasks.register("buildJava17") {
    group = "distribution"
    description = "Distribute Build Java 17"

//    Pakai DistZip
    finalizedBy("distZip")

// Ubah Option sesuai versi
    doFirst {
        println("Build Untuk versi Java 17")
        val compileJava = tasks.named<JavaCompile>("compileJava").get()
        compileJava.options.release.set(17)
    }

//    Sebelum di distZip
    doLast {
        tasks.named<Zip>("distZip") {
            archiveFileName.set("${application.applicationName}-Java17.zip")
        }
    }
}

// Task Buat Build Java Version 26
tasks.register("buildJava26") {
    group = "distribution"
    description = "Distribute Build Java 26"
    finalizedBy("distZip")

    doFirst {
        println("Build Untuk versi Java 26")
        val compileJava = tasks.named<JavaCompile>("compileJava").get()
        compileJava.options.release.set(26)
    }

    doLast {
        tasks.named<Zip>("distZip") {
            archiveFileName.set("${application.applicationName}-Java26.zip")
        }
    }

}


application {
    mainClass.set("com.github.orions29.ekspedisi.Main")
    // Diperlukan agar BridJ (OpenImajDriver) bisa load native library
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}


tasks.test {
    useJUnitPlatform()
}