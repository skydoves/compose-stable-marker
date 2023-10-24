/*
 * Designed and developed by 2023 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("UnstableApiUsage")

import com.github.skydoves.Configuration

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.dokka.get().pluginId)
  id(libs.plugins.nexus.plugin.get().pluginId)
}

kotlin {
  jvmToolchain(11)
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    }
    withJava()
  }
  ios()
  js(IR) {
    browser()
    binaries.executable()
  }
  iosSimulatorArm64()
  macosArm64()
  macosX64()

  sourceSets {
    val commonMain by getting
    val commonTest by getting

    val jvmMain by getting
    val jvmTest by getting

    val appleMain by creating {
      dependsOn(commonMain)
    }
    val appleTest by creating {
      dependsOn(commonTest)
    }

    val iosMain by getting {
      dependsOn(appleMain)
    }
    val macosArm64Main by getting {
      dependsOn(appleMain)
    }
    val macosX64Main by getting {
      dependsOn(appleMain)
    }
    val iosSimulatorArm64Main by getting {
      dependsOn(appleMain)
    }

    val iosArm64Main by getting {
      dependsOn(appleTest)
    }
    val iosArm64Test by getting {
      dependsOn(appleTest)
    }

    val iosX64Main by getting {
      dependsOn(appleTest)
    }
    val iosX64Test by getting {
      dependsOn(appleTest)
    }

    val iosTest by getting {
      dependsOn(appleTest)
    }
    val iosSimulatorArm64Test by getting {
      dependsOn(appleTest)
    }
    val macosX64Test by getting {
      dependsOn(appleTest)
    }
    val macosArm64Test by getting {
      dependsOn(appleTest)
    }
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    kotlinOptions.freeCompilerArgs += listOf(
      "-Xexplicit-api=strict"
    )
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType(JavaCompile::class.java).configureEach {
  this.targetCompatibility = JavaVersion.VERSION_11.toString()
  this.sourceCompatibility = JavaVersion.VERSION_11.toString()
}