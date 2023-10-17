## Just an Android project with some stuff that I prefer.

> `base-v*` contains common utils for Android View system.</br>
> `compose-v*` contains common utils for Android Compose.</br>
> `v*` contains both.</br>

<img src="https://github.com/oOJohn6Oo/BaseAndroid/assets/24718357/3e4d5c27-35fe-40da-a1b0-592c9a5f16d5" width="400" />


### Main Features

<details>
  
<summary> InsetsHelper -- Handling WindowInsets easily </summary>

[InsetsHelper]

> Included in `base-*`, used for handling WindowInsets.

* LifeCycle aware, auto-release when activity destroyed.
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
  
<summary> ProgressHelper -- LifeCycle aware Progress Dialog </summary>

[ProgressHelper]

> Included in `base-*`, used to show a normal prompt.

* Support delay trigger.
* LifeCycle aware, auto-release when activity destroyed.
* Avoid duplicate, all components in the same activity share one dialog.

</details>

<details>
  
<summary> JohnAppTheme Pre-set Compose Theme </summary>

[JohnAppTheme]

> Included in `compose-*`, used for quickly handling staff related to the status bar and navigation bar.

* Include all [InsetsHelper] features.

</details>

<details>

<summary> JTooltips One line of code to create Tooltips</summary>

[JTooltips]

> Included in `base-*`, used for show tooltips easily.<br>
> With the extension function we can auto-calculate view position and tip stick offset

<img src="https://github.com/oOJohn6Oo/BaseAndroid/assets/24718357/dce83869-2d1e-4a0f-8188-a62a7a3d9d50" width="400" />

* [JTooltipsLayout] extends from FrameLayout, supporting custom material edge treatment.

  | attr | desc |
  | :- |  :- |
  | tipEdge | change tip stick positions, support `edgeStart`,`edgeTop`,`edgeEnd`,`edgeBottom`,`edgeHorizontal`,`edgeVertical`,`edgeAll` |
  | tipDrawGravity | change tip stick draw direction, support `start`, `center`, `end` |
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
  
* [JDefaultTooltips] extends [JTooltipsLayout], and has a fixed inner layout, used to speed up common requirements.
  > It is a text followed by a divider and close icon by default, which can be customized using the following attrs.

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
  
<summary> JWheelPicker iOS-style wheel picker</summary>

[JWheelPicker]

* iOS-style wheel picker
* Haptic Feedback
* For single wheel picker, use [JSinglePickerDialogFragment], need to pass List<T>
* For multiple wheel pickers, use [JMultiplePickerDialogFragment], need to implement your own [IMultipleJPickerAdapter]

</details>

### ~~Using GitHub Actions to push Packages~~

Deprecated: since GitHub Packages do not allow public access.

### implementation

- add repo `maven { url 'https://jitpack.io' }`
- latest version ![JitPack Icon]
- add `implementation("com.github.oOJohn6Oo.BaseAndroid:john-base:<version>")`


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
