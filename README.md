## Just an Android project with some stuff that I prefer.

> `base-v*` contains common utils for Android View system.</br>
> `compose-v*` contains common utils for Android Compose.</br>
> `v*` contains both.</br>

### ~~Using GitHub Actions to push Packages~~

Deprecated: since GitHub Packages do not allow public access.

But I will still publish on it.

### implementation

- add repo `maven { url 'https://jitpack.io' }`
- latest version ![JitPack Icon]
- add `implementation("com.github.oOJohn6Oo.BaseAndroid:john-base:<version>")`

### Main Features

#### [InsetsHelper]

Included in `base-v*`, used for handling WindowInsets.

* LifeCycle aware, auto release when activity destroyed.
* Auto handle WindowInsets.
* Auto handle SystemBar Scrim.
* Support Gesture Navigation detection.
* Support Dark Mode.

Usage:

https://github.com/oOJohn6Oo/BaseAndroid/blob/0c6dc5d4c24ed979a06dc536311a592a4f464473/JohnBase/src/main/java/com/john6/johnbase/util/InsetsHelper.kt#L31-L43

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