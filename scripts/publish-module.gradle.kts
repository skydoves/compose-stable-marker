import com.github.skydoves.Configurations

apply(plugin = "com.vanniktech.maven.publish")

rootProject.extra.apply {
  val snapshot = System.getenv("SNAPSHOT").toBoolean()
  val libVersion = if (snapshot) {
    Configurations.snapshotVersionName
  } else {
    Configurations.versionName
  }
  set("libVersion", libVersion)
}
