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
import com.github.skydoves.Configuration

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("java-library")
  id("kotlin")
  id(libs.plugins.dokka.get().pluginId)
}

rootProject.extra.apply {
  set("PUBLISH_GROUP_ID", Configuration.artifactGroup)
  set("PUBLISH_ARTIFACT_ID", "compose-stable-marker")
  set("PUBLISH_VERSION", rootProject.extra.get("rootVersionName"))
}

apply(from ="${rootDir}/scripts/publish-module.gradle")

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.freeCompilerArgs += listOf(
    "-Xexplicit-api=strict"
  )
}
