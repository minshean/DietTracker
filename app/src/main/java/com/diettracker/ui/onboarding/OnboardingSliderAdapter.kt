package com.diettracker.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class OnboardingSliderAdapter(
    fragmentManager: FragmentManager,
) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Page1()
            1 -> Page2()
            2 -> Page3()
            else -> throw IllegalArgumentException("Invalid Position")
        }
    }

}