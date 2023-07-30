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

#### [JTooltips]
Included in `base-v*`, used for show tooltips easily.

* [JTooltipsLayout] extend from FrameLayout, support custom material edge treatment.
  
  | attr | desc | pic |
  | :-: |  :-: | :-: |
  | tipEdge | change tip stick positions | todo |
  | tipDrawGravity | change tip stick draw direction | todo |
  | tipDrawOffset |  offset tip stick | todo |
  | tipDrawStyle |  support `triangle`,`roundTriangle`,`circle`,`defaultLine` | todo |
  | tipDrawInside |  whether tip stick inside or outside view | todo |
  | tipDepth |  tip stick height | todo |
  | tipWidth |  tip stick width | todo |
  | shapeCornerRadius | support fraction and dimension | todo |
  | android:insetLeft | as the name says | todo |
  | android:insetTop | as the name says | todo |
  | android:insetRight | as the name says | todo |
  | android:insetBottom | as the name says | todo |
  | android:strokeColor | as the name says | todo |
  | strokeWidth | as the name says | todo |
  | android:backgroundTint | as the name says | todo |
  | android:backgroundTintMode | as the name says | todo |
  | rippleColor | as the name says | todo |
  | android:elevation | as the name says | todo |
  | android:background | as the name says, but affects all the other attrs | todo |
  
* [JDefaultTooltips] extends [JTooltipsLayout], have fixed inner layout, used for speed up common requirements.
> It is a text followed by a divider and close icon by default, can customize using following attrs.

| attr | desc |
| :- | :- |
| android:text | as the name says |
| android:textColor | as the name says |
| android:textSize | as the name says |
| android:drawableStart | as the name says |
| android:drawablePadding | as the name says |
| android:src | as the name says |
| disableTailIcon | hide the divider and icon |

[JitPack Icon]: https://jitpack.io/v/oOJohn6Oo/BaseAndroid.svg
[InsetsHelper]: ./JohnBase/src/main/kotlin/com/john6/johnbase/util/InsetsHelper.kt
[ProgressHelper]: ./JohnBase/src/main/kotlin/com/john6/johnbase/util/LoadingHelper.kt
[JohnAppTheme]: ./JohnBaseCompose/src/main/kotlin/io/john6/johnbase/compose/AppTheme.kt
[JTooltips]: ./JohnBase/src/main/kotlin/com/john6/johnbase/util/tooltips
[JTooltipsLayout]: ./JohnBase/src/main/kotlin/com/john6/johnbase/util/tooltips/JTooltipsLayout.kt
[JDefaultTooltips]: ./JohnBase/src/main/kotlin/com/john6/johnbase/util/tooltips/JDefaultTooltips.kt