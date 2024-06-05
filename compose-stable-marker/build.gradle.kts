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

import com.github.skydoves.Configurations
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.dokka.get().pluginId)
  id(libs.plugins.nexus.plugin.get().pluginId)
}

apply(from = "${rootDir}/scripts/publish-module.gradle.kts")

mavenPublishing {
  val artifactId = "compose-stable-marker"
  coordinates(
    Configurations.artifactGroup,
    artifactId,
    rootProject.extra.get("libVersion").toString()
  )

  pom {
    name.set(artifactId)
    description.set("Compose stable markers for KMP to tell stable/immutable guarantees to the compose compiler.")
  }
}

kotlin {
  jvmToolchain(11)
  androidTarget { publishLibraryVariants("release") }
  jvm("desktop")
  iosX64()
  iosArm64()
  iosSimulatorArm64()
  macosX64()
  macosArm64()

  js {
    browser()
    nodejs {
      testTask {
        useMocha {
          timeout = "60s"
        }
      }
    }
    binaries.executable()
    binaries.library()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser {
      testTask {
        enabled = false
      }
    }
    nodejs {
      testTask {
        enabled = false
      }
    }
    binaries.executable()
    binaries.library()
  }

  @Suppress("OPT_IN_USAGE")
  applyHierarchyTemplate {
    common {
      group("jvm") {
        withAndroidTarget()
        withJvm()
      }
      group("skia") {
        withJvm()
        group("darwin") {
          group("apple") {
            group("ios") {
              withIosX64()
              withIosArm64()
              withIosSimulatorArm64()
            }
            group("macos") {
              withMacosX64()
              withMacosArm64()
            }
          }
          withJs()
          withWasmJs()
        }
      }
    }
  }

  explicitApi()
  applyKotlinJsImplicitDependencyWorkaround()
}

android {
  compileSdk = Configurations.compileSdk
  namespace = "com.skydoves.compose.stable.marker"
  defaultConfig {
    minSdk = Configurations.minSdk
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

// https://youtrack.jetbrains.com/issue/KT-56025
fun Project.applyKotlinJsImplicitDependencyWorkaround() {
  tasks {
    val configureJs: Task.() -> Unit = {
      dependsOn(named("jsDevelopmentLibraryCompileSync"))
      dependsOn(named("jsDevelopmentExecutableCompileSync"))
      dependsOn(named("jsProductionLibraryCompileSync"))
      dependsOn(named("jsProductionExecutableCompileSync"))
      dependsOn(named("jsTestTestDevelopmentExecutableCompileSync"))

      dependsOn(getByPath(":compose-stable-marker:jsDevelopmentLibraryCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:jsDevelopmentExecutableCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:jsProductionLibraryCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:jsProductionExecutableCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:jsTestTestDevelopmentExecutableCompileSync"))
    }
    named("jsBrowserProductionWebpack").configure(configureJs)
    named("jsBrowserProductionLibraryDistribution").configure(configureJs)
    named("jsNodeProductionLibraryDistribution").configure(configureJs)

    val configureWasmJs: Task.() -> Unit = {
      dependsOn(named("wasmJsDevelopmentLibraryCompileSync"))
      dependsOn(named("wasmJsDevelopmentExecutableCompileSync"))
      dependsOn(named("wasmJsProductionLibraryCompileSync"))
      dependsOn(named("wasmJsProductionExecutableCompileSync"))
      dependsOn(named("wasmJsTestTestDevelopmentExecutableCompileSync"))

      dependsOn(getByPath(":compose-stable-marker:wasmJsDevelopmentLibraryCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:wasmJsDevelopmentExecutableCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:wasmJsProductionLibraryCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:wasmJsProductionExecutableCompileSync"))
      dependsOn(getByPath(":compose-stable-marker:wasmJsTestTestDevelopmentExecutableCompileSync"))
    }
    named("wasmJsBrowserProductionWebpack").configure(configureWasmJs)
    named("wasmJsBrowserProductionLibraryDistribution").configure(configureWasmJs)
    named("wasmJsNodeProductionLibraryDistribution").configure(configureWasmJs)
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