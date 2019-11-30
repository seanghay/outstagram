package com.seanghay.outstagram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

class OutstagramActivity : AppCompatActivity() {

    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Outstagram)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outstagram)
        navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
    }

    override fun onSupportNavigateUp(): Boolean {
        if (::navHost.isInitialized)
            return navHost.navController.navigateUp()
        return super.onSupportNavigateUp()
    }

}
