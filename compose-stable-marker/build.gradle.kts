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

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64(),
    macosArm64(),
    macosX64(),
  ).forEach {
    it.binaries.framework {
      baseName = "common"
    }
  }

  androidTarget {
    publishLibraryVariants("release")
  }

  jvm {
    libs.versions.jvmTarget.get().toInt()
    compilations.all {
      kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    }
  }

  applyDefaultHierarchyTemplate()

  sourceSets {
    explicitApi()
  }
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

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType(JavaCompile::class.java).configureEach {
  this.targetCompatibility = JavaVersion.VERSION_11.toString()
  this.sourceCompatibility = JavaVersion.VERSION_11.toString()
}