package com.jsuka.breweryscheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jsuka.breweryscheduler.ui.AppScaffold
import com.jsuka.breweryscheduler.ui.theme.BreweryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as BreweryApplication).container

        setContent {
            BreweryTheme {
                AppScaffold(container = container)
            }
        }
    }
}
