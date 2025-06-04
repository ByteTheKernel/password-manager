package com.example.password_manager

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.password_manager.databinding.ActivityMainBinding
import androidx.core.view.WindowCompat
import android.view.ViewTreeObserver


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, statusBarHeight, 0, 0)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.passwordsFragment -> {
                    navController.navigate(R.id.passwordsFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }

        binding.fabAdd.setOnClickListener {
            navController.navigate(R.id.addEditPasswordFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isAuth = destination.id == R.id.authFragment
            val isAddEdit = destination.id == R.id.addEditPasswordFragment

            binding.bottomNavigation.isVisible = !isAuth
            binding.fabAdd.isVisible = !isAuth

            binding.fabAdd.setOnClickListener(
                if (isAddEdit) {
                    {}
                } else {
                    { navController.navigate(R.id.addEditPasswordFragment) }
                }
            )
        }
    }
}
