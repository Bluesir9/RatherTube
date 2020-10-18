import org.jetbrains.kotlin.config.KotlinCompilerVersion

val kotlinCoroutinesVersion = "1.3.5"
val ktorVersion = "1.3.2"
val kotlinSerializationVersion = "0.20.0"
val klockVersion = "1.10.3"

plugins {
  id("org.jetbrains.kotlin.js") version "1.3.72"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.3.72"
}

group = "com.bluesir9"
version = "0.1.0"

repositories {
  maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-js", KotlinCompilerVersion.VERSION))

  //region Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$kotlinCoroutinesVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinCoroutinesVersion")
  //endregion

  implementation(npm("uuid"))

  //region Ktor
  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-js:$ktorVersion")
  implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
  implementation("io.ktor:ktor-client-logging-js:$ktorVersion")
  //endregion

  //region Module missing error fixes
  /*
  After I integrated and started using Ktor in the project,
  the project would give build errors saying these modules
  were missing. A quick Google search lead to me to this
  Github issue ->

  https://github.com/ktorio/ktor/issues/1400

  One of the instructions provided there was to declare
  an npm dependency for each of these modules that were
  reported as missing, and upon doing this the project
  started building again. Should probably follow up on
  why this continues to be a problem.
  Hence FIXME.
  */
  implementation(npm("bufferutil"))
  implementation(npm("utf-8-validate"))
  implementation(npm("abort-controller"))
  implementation(npm("text-encoding"))
  implementation(npm("fs"))
  //endregion

  implementation("com.soywiz.korlibs.klock:klock-js:$klockVersion")
}

kotlin.target.browser { }
