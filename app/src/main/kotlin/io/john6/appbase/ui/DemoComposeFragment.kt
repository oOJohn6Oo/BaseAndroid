package io.john6.appbase.ui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.picker.JWheelPickerHelper
import io.john6.johnbase.compose.picker.bean.JWheelPickerItemInfo
import io.john6.johnbase.compose.picker.dialog.multiple.IMultipleJPickerAdapter
import io.john6.johnbase.compose.picker.dialog.multiple.JMultiPickerDialogData
import io.john6.johnbase.compose.picker.dialog.multiple.JMultiplePickerDialogFragment
import io.john6.johnbase.compose.picker.dialog.single.JSinglePickerDialogData
import io.john6.johnbase.compose.picker.dialog.single.JSinglePickerDialogFragment
import io.john6.johnbase.compose.spaceLarge

class DemoComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            DemoComposeScreen(
                showSinglePicker = this@DemoComposeFragment::showSinglePicker,
                showMultiplePicker = this@DemoComposeFragment::showMultiplePicker
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            JSinglePickerDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getParcelableCompat("result", JWheelPickerItemInfo::class.java)
            Toast.makeText(requireContext(), result?.id.toString(), Toast.LENGTH_SHORT).show()
        }
        parentFragmentManager.setFragmentResultListener(
            JMultiplePickerDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getParcelableArrayCompat("result", JWheelPickerItemInfo::class.java)
            Toast.makeText(requireContext(), result?.joinToString { it.id }, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showSinglePicker() {
        JSinglePickerDialogFragment.show(
            parentFragmentManager,
            requiredData = JSinglePickerDialogData(
                title = 0 to "SinglePicker",
                initialIndex = 2,
                dataList = ArrayList((0..3).map {
                    JWheelPickerItemInfo(
                        it.toString(),
                        it,
                        "item$it"
                    )
                }),
                overlayStyle = JWheelPickerHelper.overlayStyleOvalRectangle
            ),
        )
    }

    private fun showMultiplePicker() {
        JMultiplePickerDialogFragment.show(
            parentFragmentManager,
            requiredData = JMultiPickerDialogData(
                title = 0 to "MultiplePicker",
                overlayStyle = JWheelPickerHelper.overlayStyleOvalRectangle,
                adapterClass = IMultipleJPickerAdapter.testAdapter::class.java
            ),
        )
    }

    @Suppress("DEPRECATION")
    private fun <T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, clazz)
        } else {
            getParcelable(key) as? T
        }
    }

    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    private fun <T : Parcelable> Bundle.getParcelableArrayCompat(
        key: String,
        clazz: Class<T>
    ): Array<T>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableArray(key, clazz)
        } else {
            getParcelableArray(key) as? Array<T>
        }
    }
}

@Composable
private fun DemoComposeScreen(
    showSinglePicker: () -> Unit,
    showMultiplePicker: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = MaterialColors.getColor(context, android.R.attr.colorPrimary, android.graphics.Color.RED)
    JohnAppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                .asPaddingValues()
        ) {
            item {
                Button(
                    onClick = showSinglePicker, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(primaryColor),
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(text = "Show Single Piker")
                }
            }
            item {
                Button(
                    onClick = showMultiplePicker, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(primaryColor),
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(text = "Show Multiple Piker")
                }
            }
            items(50) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.spaceLarge)
                ) {
                    Text(text = "Demo Compose Screen", color = MaterialTheme.colors.onBackground)
                }
            }
        }
    }
}