<h1 align="center">Compose Stable Marker</h1></br>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/skydoves/compose-stable-marker/actions/workflows/android.yml"><img alt="Build Status" 
  src="https://github.com/skydoves/compose-stable-marker/actions/workflows/android.yml/badge.svg"/></a>
  <a href="https://github.com/skydoves"><img alt="Profile" src="https://skydoves.github.io/badges/skydoves.svg"/></a>
</p><br>

<p align="center">
✒️ Compose stable markers were originated Compose runtime, which improves Compose performance by telling stable and skippable guarantees to the compose compiler from non-compose dependent modules. This library supports Kotlin Multiplatform.
</p><br>

<p align="center">
<img src="https://github.com/skydoves/compose-stable-marker/assets/24237865/9ead142c-3a35-4027-932a-b1d4e1cd13c5" />
</p>

## Agenda

This library contains a few extracted Compose stable markers, such as [Stable](https://developer.android.com/reference/kotlin/androidx/compose/runtime/Stable), [Immutable](https://developer.android.com/reference/kotlin/androidx/compose/runtime/Immutable), and [StableMarker](https://developer.android.com/reference/kotlin/androidx/compose/runtime/StableMarker), which are used to communicate some guarantees to the compose compiler and mark class as producing immutable instances. You can utilize this library when you want to mark your properties/classes/functions with **Stable** or **Immutable**, which are from [compose-runtime](https://developer.android.com/jetpack/androidx/releases/compose-runtime) in your pure Kotlin module or non-compose dependent modules. So if you don't want to depend on the `compose-runtime` library, but you still want to improve your Compose performance by marking your models as stable in multiple module structure, you can use this library. If you want to learn more about **Skippable**, **Stable**, and **Immutable**, check out [6 Jetpack Compose Guidelines to Optimize Your App Performance](https://medium.com/proandroiddev/6-jetpack-compose-guidelines-to-optimize-your-app-performance-be18533721f9) and [Composable metrics by Chris Banes](https://chrisbanes.me/posts/composable-metrics/#skippable).

## Download
[![Maven Central](https://img.shields.io/maven-central/v/com.github.skydoves/compose-stable-marker.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%compose-stable-marker%22)

### Gradle

Add the `compileOnly` dependency below to your **module**'s `build.gradle.kts` file:

```gradle
dependencies {
    compileOnly("com.github.skydoves:compose-stable-marker:1.0.2")
}
```

For Kotlin Multiplatform, add the `compileOnly` dependency below to your **module**'s `build.gradle.kts` file:

```gradle
sourceSets {
    val commonMain by getting {
        dependencies {
            compileOnly("com.github.skydoves:compose-stable-marker:$version")
        }
    }
}
```

## Stable

These hold data that is mutable but notify Composition upon mutating. This renders them stable since Composition is always informed of any changes to the state. This annotation implies are used for optimizations by the compose compiler if the assumptions below are met:

1.  The result of `equals` will always return the same result for the same two instances.
 2. When a public property of the type changes, composition will be notified.
 3. All public property types are stable.

You can utilize the [Stable](https://developer.android.com/reference/kotlin/androidx/compose/runtime/Stable) annotation like a normal annotation in your pure Kotlin module or non-compose dependent modules. <br><br>

### Without Stable Annotation (Unskippable, and Unstable)

Let's assume that you have a normal class without the `Stable` annotation:

```kotlin
// data module (pure Kotlin module)
public data class UnstableUser(
  public val name: String,
  public val devices: List<String>,
  public val createdAt: Instant,
)

// feature module
@Composable
private fun UnstableUserFun(unstableUser: UnstableUser) {
  Text(text = unstableUser.toString())
}
```

Following the [Compose Compiler Metrics](https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md), you'll get the result below:

```
restartable scheme("[androidx.compose.ui.UiComposable]") fun UnstableUserFun(
  unstable unstableUser: UnstableUser
)
```

As you can see in the above metrics, the `UnstableUserFun` Composable function is not **Skippable**, and the `UnstableUser` class is marked as **unstable**. 

### With Stable Annotation (Skippable, and Stable)

Let's assume that you have a normal class with the `Stable` annotation:

```kotlin
// data module (pure Kotlin module)
@Stable
public data class StableUser(
  public val name: String,
  public val devices: List<String>,
  public val createdAt: Instant,
)

// feature module
@Composable
private fun StableUserFun(stableUser: StableUser) {
  Text(text = stableUser.toString())
}
```

Following the [Compose Compiler Metrics](https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md), you'll get the result below:

```
restartable skippable scheme("[androidx.compose.ui.UiComposable]") fun StableUserFun(
  stable stableUser: StableUser
)
```

As you can see in the above metrics, the `StableUserFun` Composable function is **Skippable**, and the `StableUser` class is marked as **stable**. So your function will be completely skipped calling a function if the parameters haven't changed since the last call since all of your properties were marked as **Stable**.

## Immutable

With the same approach of the `Stable` annotation, you can use this annotation like a normal `compose-runtime`'s one. As the name suggests, these hold data that is immutable. Since the data never changes, Compose can treat this as stable data. Composition enables optimizations based on the assumption that values read from the type will not change, using this annotation. <br><br>

Let's assume that you have a normal class with the `Immutable` annotation:

```kotlin
// data module (pure Kotlin module)
@Immutable
public data class ImmutableUser constructor(
  public val name: String,
  public val devices: List<String>,
  public val createdAt: Instant,
)

// feature module
@Composable
private fun ImmutableUserComposable(immutableUser: ImmutableUser) {
  Text(text = immutableUser.toString())
}
```

Following the [Compose Compiler Metrics](https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md), you'll get the result below:

```
restartable skippable scheme("[androidx.compose.ui.UiComposable]") fun ImmutableUser(
  stable immutableUser: ImmutableUser
)
```

## StableMarker

An annotation marked as `StableMarker` indicates a stable type, which obeys the following assumptions:

 1. The result of [equals] will always return the same result for the same two instances.
 2. When a public property of the type changes, composition will be notified.
 3. All public property types are stable.

# License
```xml
Designed and developed by 2023 skydoves (Jaewoong Eum)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
