package com.diettracker.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.diettracker.R
import com.diettracker.core.location.GPSManager
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.databinding.FragmentHomeBinding
import com.diettracker.ui.home.tabs.diet.DietFragment
import com.diettracker.ui.home.tabs.fitness.FitnessFragment
import com.diettracker.ui.home.tabs.items.ItemsFragment
import com.diettracker.ui.home.tabs.profile.ProfileFragment
import com.diettracker.ui.home.tabs.recipes.RecipesFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private lateinit var gpsManager: GPSManager

    private val navigationDispatcher by inject<NavigationDispatcher>()

    private val homeViewModel by sharedViewModel<HomeViewModel>()

    private lateinit var recipesFragment: RecipesFragment
    private lateinit var dietFragment: DietFragment
//    private lateinit var itemsFragment: ItemsFragment
    private lateinit var fitnessFragment: FitnessFragment
    private lateinit var profileFragment: ProfileFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(
            recipesFragment,
            dietFragment,
            fitnessFragment,
            profileFragment)

    private var selectedIndex = 0

    private val tabs: Array<ImageView>
        get() = binding.run {
            arrayOf(buttonTabFirst, buttonTabSecond, buttonTabThird, buttonTabFourth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uploadNotificationToken()

        gpsManager = GPSManager(requireContext())

        if (savedInstanceState == null) {
            val recipesFragment = RecipesFragment().also { this.recipesFragment = it }
            val dietFragment = DietFragment().also { this.dietFragment = it }
            val fitnessFragment = FitnessFragment().also { this.fitnessFragment = it }
            val profileFragment = ProfileFragment().also { this.profileFragment = it }

            childFragmentManager.beginTransaction()
                .add(R.id.homeContainer, recipesFragment, "firstFragment")
                .add(R.id.homeContainer, dietFragment, "secondFragment")
                .add(R.id.homeContainer, fitnessFragment, "thirdFragment")
                .add(R.id.homeContainer, profileFragment, "fourthFragment")
                .selectFragment(selectedIndex)
                .commit()
        } else {
            selectedIndex = savedInstanceState.getInt("selectedIndex", 0)

            recipesFragment = childFragmentManager.findFragmentByTag("firstFragment") as RecipesFragment
            dietFragment = childFragmentManager.findFragmentByTag("secondFragment") as DietFragment
            fitnessFragment =
                childFragmentManager.findFragmentByTag("thirdFragment") as FitnessFragment
            profileFragment =
                childFragmentManager.findFragmentByTag("fourthFragment") as ProfileFragment
        }
    }

    private fun uploadNotificationToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseDatabase.getInstance().reference.child("Tokens")
                .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                .setValue(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdd.setOnClickListener {
            navigationDispatcher.emit {
                navigate(R.id.action_homeFragment_to_addRecipeFragment)
            }
        }

        homeViewModel.openExerciseTab.observe(viewLifecycleOwner){
            it?.let {
                selectFragment(2, false)
                homeViewModel.openExerciseTab.postValue(null)
            }
        }

        tabs.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                selectFragment(index)
            }
        }

        val btn_maps = view.findViewById<ImageView>(R.id.buttonTabThird)
        btn_maps.setOnClickListener(View.OnClickListener { gotoMaps("4.330436", "101.137271") })

        setupTabSelectedState(selectedIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedIndex", selectedIndex)
    }

    private fun setupTabSelectedState(selectedIndex: Int) {
        binding.imageView.isVisible = selectedIndex != 1
        binding.root.setBackgroundColor(
            if (selectedIndex == 1) {
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            } else {
                ContextCompat.getColor(requireContext(), R.color.white)
            }
        )
        tabs.forEachIndexed { index, textView ->
            textView.setBackgroundResource(when (index) {
                selectedIndex -> R.drawable.icon_5
                else -> R.drawable.icon_6
            })
        }
    }

    private fun FragmentTransaction.selectFragment(selectedIndex: Int): FragmentTransaction {
        fragments.forEachIndexed { index, fragment ->
            if (index == selectedIndex) {
                attach(fragment)
            } else {
                detach(fragment)
            }
        }

        return this
    }

    private fun checkGPSEnable() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id
                ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun selectFragment(indexToSelect: Int, default: Boolean = true) {

        if (indexToSelect == 2 && default) {

            val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                checkGPSEnable()
                return
            }

            binding.progressBar.isVisible = true
            gpsManager.startListening(requireContext())
            gpsManager.setGPSCallback {
                if (it != null) {
                    gotoMaps(it)
                    gpsManager.stopListening()
                    binding.progressBar.isVisible = false
                }
            }
//            }
            return
        }

        this.selectedIndex = indexToSelect

        setupTabSelectedState(indexToSelect)

        childFragmentManager.beginTransaction()
            .selectFragment(indexToSelect)
            .commit()
    }

    private fun gotoMaps(it: Location) {
        Log.d("GMaps", "Going to Maps");
        val intentUri = Uri.parse("geo:${it.latitude},${it.longitude}?q=restaurants")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun gotoMaps(lat: String, long: String) {
        Log.d("GMaps", "Going to Maps");
        val intentUri = Uri.parse("geo:${lat},${long}?q=restaurants")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}