plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14" apply true
}

group = "net.masternation"
version = "1.0.0"

tasks {
    shadowJar {
        archiveBaseName.set("vira-wallet")
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://storehouse.okaeri.eu/repository/maven-public")
    }
    maven {
        url = uri("https://nexus.iridiumdevelopment.net/repository/maven-releases/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")
    pluginRemapper("net.fabricmc:tiny-remapper:0.10.3:fat")

    compileOnly("org.springframework:spring-context:6.1.4")

    implementation("me.masterkaiser.farmework:core:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:bukkit:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api_v1_18_R1:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api_v1_19_R1:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api_v1_20_common:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api_v1_20_R1:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api_v1_20_R4:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api_v1_21_common:1.0-SNAPSHOT")
    implementation("me.masterkaiser.bukkit:api_v1_21_R1:1.0-SNAPSHOT")
    implementation("me.masterkaiser.farmework:core2:1.0-SNAPSHOT")

    // #okaeriConfigs
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.0-beta.5")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:5.0.0-beta.5")
    implementation("eu.okaeri:okaeri-configs-json-gson:5.0.0-beta.5")
    implementation("eu.okaeri:okaeri-configs-json-simple:5.0.0-beta.5")

    // #okaeriPersistence
    implementation("eu.okaeri:okaeri-persistence-core:2.0.3")
    implementation("eu.okaeri:okaeri-persistence-flat:2.0.3")
    implementation("eu.okaeri:okaeri-persistence-jdbc:2.0.3")

    // #okaeriTasker
    implementation("eu.okaeri:okaeri-tasker-bukkit:2.1.0-beta.3")

    // #IridiumColorAPI
    implementation("com.iridium:IridiumColorAPI:1.0.9")

    // #PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.6")
}
