package com.john6.appbase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import io.john6.johnbase.compose.JohnAppTheme
import io.john6.johnbase.compose.spaceMedium

class DemoComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            DemoComposeScreen()
        }
    }
}

@Composable
private fun DemoComposeScreen() {
    JohnAppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                .add(
                    WindowInsets(
                        left = MaterialTheme.spaceMedium,
                        right = MaterialTheme.spaceMedium,
                    )
                ).asPaddingValues()
        ) {
            items(100) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Demo Compose Screen", color = MaterialTheme.colors.onBackground)
                }
            }
        }
    }
}