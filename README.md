## Just an Android project with some stuff that I prefer.

> `base-v*` contains common utils for Android View system.</br>
> `compose-v*` contains common utils for Android Compose.</br>
> `v*` contains both.</br>

<img src="https://github.com/oOJohn6Oo/BaseAndroid/assets/24718357/3e4d5c27-35fe-40da-a1b0-592c9a5f16d5" width="400" />

### ~~Using GitHub Actions to push Packages~~

Deprecated: since GitHub Packages do not allow public access.

But I will still publish on it.

### implementation

- add repo `maven { url 'https://jitpack.io' }`
- latest version ![JitPack Icon]
- add `implementation("com.github.oOJohn6Oo.BaseAndroid:john-base:<version>")`

### Main Features

<details>
  
<summary> InsetsHelper </summary>

[InsetsHelper]

> Included in `base-*`, used for handling WindowInsets.

* LifeCycle aware, auto release when activity destroyed.
* Auto handle WindowInsets.
* Auto handle SystemBar Scrim.
* Support Gesture Navigation detection.
* Support Dark Mode.

Usage:

``` kotlin
class MyActivity: AppCompatActivity() { 
    private val insetsHelper = InsetsHelper() 
    override fun onCreate(savedInstanceState: Bundle?) { 
        super.onCreate(savedInstanceState) 
        lifecycle.addObserver(insetsHelper) 
        // init ViewBinding 
        // Config Inset View 
        insetsHelper.statusBarResponseView = binding.toolBarAttMain 
        insetsHelper.navBarResponseView = binding.recycleViewAttMain 
    }
} 
```

</details>

<details>
  
<summary> ProgressHelper </summary>

[ProgressHelper]

> Included in `base-*`, used for show normal prompt.

* Support delay trigger.
* LifeCycle aware, auto release when activity destroyed.
* Avoid duplicate, all components in same activity shares one dialog.

</details>

<details>
  
<summary> JohnAppTheme </summary>

[JohnAppTheme]

> Included in `compose-*`, used for quickly handle staff related to status bar and navigation bar.

* Include all [InsetsHelper] features.

</details>

<details>

<summary> JTooltips </summary>

[JTooltips]

> Included in `base-*`, used for show tooltips easily.<br>
> With extension function we can auto calculate view position and tip stick offset

<img src="https://github.com/oOJohn6Oo/BaseAndroid/assets/24718357/86464dd1-fc60-47c1-9210-e867a83a3f61" width="400" />

* [JTooltipsLayout] extend from FrameLayout, support custom material edge treatment.

  | attr | desc |
  | :- |  :- |
  | tipEdge | change tip stick positions, support `edgeStart`,`edgeTop`,`edgeEnd`,`edgeBottom`,`edgeHorizontal`,`edgeVertical`,`edgeAll` |
  | tipDrawGravity | change tip stick draw direction, support `start`,`center`,`end` |
  | tipDrawOffset |  offset tip stick |
  | tipDrawStyle |  support `triangle`,`roundTriangle`,`circle`,`defaultLine` |
  | tipDrawInside |  whether tip stick inside or outside view |
  | tipDepth |  tip stick height |
  | tipWidth |  tip stick width |
  | shapeCornerRadius | support fraction and dimension |
  | android:insetLeft | as the name says |
  | android:insetTop | as the name says |
  | android:insetRight | as the name says |
  | android:insetBottom | as the name says |
  | android:strokeColor | as the name says |
  | strokeWidth | as the name says |
  | android:backgroundTint | as the name says |
  | android:backgroundTintMode | as the name says |
  | rippleColor | as the name says |
  | android:elevation | as the name says |
  | android:background | as the name says, but affects all the other background related attrs |
  
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

</details>

<details>
  
<summary> JWheelPicker </summary>

[JWheelPicker]

* iOS style wheel picker
* For single wheel picker, use [JSinglePickerDialogFragment], need to pass List<T>
* For multiple wheel picker, use [JMultiplePickerDialogFragment], need to implement your own [IMultipleJPickerAdapter]

</details>

[JitPack Icon]: https://jitpack.io/v/oOJohn6Oo/BaseAndroid.svg
[InsetsHelper]: ./JohnBase/src/main/kotlin/io/john6/johnbase/util/InsetsHelper.kt
[ProgressHelper]: ./JohnBase/src/main/kotlin/io/john6/johnbase/util/LoadingHelper.kt
[JohnAppTheme]: ./JohnBaseCompose/src/main/kotlin/io/john6/johnbase/compose/AppTheme.kt
[JTooltips]: ./JohnBase/src/main/kotlin/io/john6/johnbase/util/tooltips
[JTooltipsLayout]: ./JohnBase/src/main/kotlin/io/john6/johnbase/util/tooltips/JTooltipsLayout.kt
[JDefaultTooltips]: ./JohnBase/src/main/kotlin/io/john6/johnbase/util/tooltips/JDefaultTooltips.kt
[JWheelPicker]: ./JWheelPicker/src/main/kotlin/io/john6/johnbase/compose/picker/JWheelPicker.kt
[JSinglePickerDialogFragment]: ./JWheelPicker/src/main/kotlin/io/john6/johnbase/compose/picker/dialog/single/JSinglePickerDialogFragment.kt
[JMultiplePickerDialogFragment]: ./JWheelPicker/src/main/kotlin/io/john6/johnbase/compose/picker/dialog/multiple/JMultiplePickerDialogFragment.kt
[IMultipleJPickerAdapter]: ./JWheelPicker/src/main/kotlin/io/john6/johnbase/compose/picker/dialog/multiple/IMultipleJPickerAdapter.kt
