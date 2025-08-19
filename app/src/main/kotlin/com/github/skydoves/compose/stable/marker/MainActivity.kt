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
package com.github.skydoves.compose.stable.marker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.skydoves.compose.stable.marker.data.ImmutableUser
import com.github.skydoves.compose.stable.marker.data.StableUser
import com.github.skydoves.compose.stable.marker.data.UnstableUser
import kotlinx.datetime.Instant
import java.util.Date

class MainActivity : ComponentActivity() {

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val stableUser = StableUser(
      name = "skydoves",
      devices = emptyList(),
      createdAt = Date.from(java.time.Instant.now()),
    )

    val unstableUser = UnstableUser(
      name = "skydoves",
      devices = emptyList(),
      createdAt = Date.from(java.time.Instant.now()),
    )

    val immutableUser = ImmutableUser(
      name = "skydoves",
      devices = emptyList(),
      createdAt = Date.from(java.time.Instant.now()),
    )

    setContent {
      Column {
        StableUserComposable(stableUser = stableUser)

        UnstableUserComposable(unstableUser = unstableUser)

        ImmutableUserComposable(immutableUser = immutableUser)
      }
    }
  }
}

/**
 * Compose Metrics:
 *
 * restartable scheme("[androidx.compose.ui.UiComposable]") fun UnstableUser(
 *   unstable unstableUser: UnstableUser
 * )
 */
@Composable
private fun UnstableUserComposable(unstableUser: UnstableUser) {
  Text(text = unstableUser.toString())
}

/**
 * Compose Metrics:
 *
 * restartable skippable scheme("[androidx.compose.ui.UiComposable]") fun StableUser(
 *   stable stableUser: StableUser
 * )
 */
@Composable
private fun StableUserComposable(stableUser: StableUser) {
  Text(text = stableUser.toString())
}

/**
 * Compose Metrics:
 *
 * restartable skippable scheme("[androidx.compose.ui.UiComposable]") fun ImmutableUser(
 *   stable immutableUser: ImmutableUser
 * )
 */
@Composable
private fun ImmutableUserComposable(immutableUser: ImmutableUser) {
  Text(text = immutableUser.toString())
}
