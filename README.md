## Just an Android project with some stuff that I prefer.

`base-v*` contains common utils for Android View system.
`compose-v*` contains common utils for Android Compose.
`v*` contains both.

### ~~Using GitHub Actions to push Packages~~

Deprecated: since GitHub Packages do not allow public access.

But I will still publish on it.

### implementation

- add repo `maven { url 'https://jitpack.io' }`
- add `implementation("com.github.oOJohn6Oo.BaseAndroid:john-base:")`![JitPack Icon]

### Main Features

#### [InsetsHelper]

Included in `base-v*`, used for handling WindowInsets.

* Support Dark Mode.
* Auto handle WindowInsets.
* LifeCycle aware, auto release when activity destroyed.
* Support Gesture Navigation detection.

#### [ProgressHelper]
Included in `base-v*`, used for show normal prompt.

* Support delay trigger.
* LifeCycle aware, auto release when activity destroyed.
* Avoid duplicate, all components in same activity shares one dialog.

#### [JohnAppTheme]

Included in `compose-v*`, used for quickly handle staff related to status bar and navigation bar.

* Include all [InsetsHelper] features.

[JitPack Icon]: https://jitpack.io/v/oOJohn6Oo/BaseAndroid.svg
[InsetsHelper]: ./JohnBase/src/main/java/com/john6/johnbase/util/InsetsHelper.kt
[ProgressHelper]: ./JohnBase/src/main/java/com/john6/johnbase/util/ProgressHelper.kt
[JohnAppTheme]: ./JohnBaseCompose/src/main/java/io/john6/johnbase/compose/AppTheme.kt