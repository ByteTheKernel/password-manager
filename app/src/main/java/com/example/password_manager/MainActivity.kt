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
import android.view.ViewTreeObserver


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupKeyboardListener()

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

    private fun setupKeyboardListener() {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            rootView.setOnApplyWindowInsetsListener { _, insets ->
                val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                binding.bottomNavigation.isVisible = !isKeyboardVisible
                binding.fabAdd.isVisible = !isKeyboardVisible
                insets
            }
        } else {
            rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                private var wasKeyboardOpen = false
                override fun onGlobalLayout() {
                    val rect = Rect()
                    rootView.getWindowVisibleDisplayFrame(rect)
                    val screenHeight = rootView.rootView.height
                    val keypadHeight = screenHeight - rect.bottom
                    val isKeyboardNowVisible = keypadHeight > screenHeight * 0.15

                    if (isKeyboardNowVisible != wasKeyboardOpen) {
                        wasKeyboardOpen = isKeyboardNowVisible
                        binding.bottomNavigation.isVisible = !isKeyboardNowVisible
                        binding.fabAdd.isVisible = !isKeyboardNowVisible
                    }
                }
            })
        }
    }
}
