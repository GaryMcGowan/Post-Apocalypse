package com.garymcgowan.postapocalypse.view

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.garymcgowan.postapocalypse.R
import com.garymcgowan.postapocalypse.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setupWithNavController(
            Navigation.findNavController(
                this, R.id.nav_host_fragment
            )
        )

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            onNavDestinationSelected(
                item, Navigation.findNavController(this, R.id.nav_host_fragment)
            )
        }
    }
}
