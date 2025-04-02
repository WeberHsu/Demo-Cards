package com.weberhsu.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.weberhsu.presentation.base.BaseActivity
import com.weberhsu.presentation.ui.cards.fragment.CardsFragment
import com.weberhsu.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindLayout: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        // Default fragment : CardsFragment
        if (savedInstanceState == null) {
            binding.bottomNav.selectedItemId = R.id.nav_cards
            replaceFragment(CardsFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_cards -> replaceFragment(CardsFragment())
                else -> {
                    replaceFragment(EmptyFragment())
                }
            }
            true
        }
    }

    @SuppressLint("CommitTransaction")
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}