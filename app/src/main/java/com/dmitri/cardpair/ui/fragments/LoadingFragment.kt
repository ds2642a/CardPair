package com.dmitri.cardpair.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.dmitri.cardpair.R
import com.dmitri.cardpair.databinding.FragmentLoadingBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class LoadingFragment : Fragment(R.layout.fragment_loading) {
    private lateinit var binding : FragmentLoadingBinding

    private var showView : Boolean = false
    private var viewUrl : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoadingBinding.bind(view)

        getFirebaseValues()
    }

    private fun getFirebaseValues() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        activity?.let {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        showView = remoteConfig.getBoolean("showView")
                        viewUrl = remoteConfig.getString("url")
                    }
                    checkFirebaseValues()
                }
        }
    }

    private fun checkFirebaseValues() {
        if (showView && !viewUrl.isNullOrBlank()) {
            navigateToViewFragment(viewUrl!!)
        } else {
            navigateToContentFragment()
        }
    }

    private fun navigateToViewFragment(viewUrl : String) {
        navigate(LoadingFragmentDirections.toViewFragment(viewUrl))
    }

    private fun navigate(action : NavDirections) {
        binding.root.findNavController()
            .navigate(action)
    }

    private fun navigateToContentFragment() {
        navigate(LoadingFragmentDirections.toContentFragment())
    }
}