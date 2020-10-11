# RatherTube

## What?
A web app that let's you listen to music off YouTube. 

## Why?
The original reasons have been invalidated now, so I intend to use this project as means of learning frontend design
while also pushing the KMP envelope by building native apps for Android, iOS, MacOS etc. 

## Where can I use this?
I haven't gotten around to hosting this app anywhere yet. 

## How do I run this?
###1. Start by cloning the project locally. 
###2. Setup the local server by following the instructions [here.](server/README.md)
###3. You will need to create a file called EnvironmentActual.kt inside the `config` package. Locally the file would look something like below:
```kotlin
package config

import io.ktor.http.*

val ENVIRONMENT = EnvironmentActual

object EnvironmentActual : Environment {
  override val debug: Boolean = true
  override val apiProtocol: URLProtocol = URLProtocol.HTTP
  override val host: String = "localhost"
}
```
###4. Try to build the project by running the `./gradlew assemble` command at the root directory. If it gives you a permission error, try running `chmod 700 gradlew` before running `./gradlew assemble` again.
###5. If you see a "BUILD SUCCESSFUL" message at the end then that means the project was setup properly.
###6. Run the app by running the `./gradlew run` command at the root directory.
###7. You should see the app launched in the browser automatically.
###8. Fin.