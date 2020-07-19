val kotlinCoroutinesVersion = "1.3.5"

plugins {
  id("org.jetbrains.kotlin.js") version "1.3.72"
}

group = "com.bluesir9"
version = "0.0.1"

repositories {
  maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-js"))

  //region Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinCoroutinesVersion")
  //endregion

  implementation(npm("uuid"))
}

kotlin.target.browser { }
