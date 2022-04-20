package com.diettracker.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.databinding.FragmentOnboardingBinding
import com.diettracker.services.session.SessionManager
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {
    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    private val sessionManager by inject<SessionManager>()
    private val navigationDispatcher by inject<NavigationDispatcher>()

    private lateinit var sliderAdapter: OnboardingSliderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnboardingViewPager()

        binding.btnNext.setOnClickListener {
            if(binding.viewpager.currentItem != 2){
                binding.viewpager.currentItem = binding.viewpager.currentItem + 1
            } else {
                sessionManager.setOnboardingSeen()
                navigationDispatcher.emit {
                    navigate(R.id.action_onboardingFragment_to_authSelectionFragment)
                }
            }
        }

        binding.btnSkipStep.setOnClickListener {
            sessionManager.setOnboardingSeen()
            navigationDispatcher.emit {
                navigate(R.id.action_onboardingFragment_to_authSelectionFragment)
            }
        }
    }

    private fun setupOnboardingViewPager() {
        sliderAdapter = OnboardingSliderAdapter(
            childFragmentManager,
        )
        binding.viewpager.apply {
            adapter = sliderAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    binding.roundOne.requestLayout()
                    binding.roundTwo.requestLayout()
                    binding.roundThree.requestLayout()
                    binding.roundOne.background =
                        resources.getDrawable(if (position == 0) R.drawable.round_active else R.drawable.round_inactive)
                    binding.roundTwo.background =
                        resources.getDrawable(if (position == 1) R.drawable.round_active else R.drawable.round_inactive)
                    binding.roundThree.background =
                        resources.getDrawable(if (position == 2) R.drawable.round_active else R.drawable.round_inactive)
                }

                override fun onPageSelected(position: Int) {
                    binding.btnSkipStep.isVisible = position != 2
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }
}