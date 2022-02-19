package com.camelloncase.trackmysleepquality.sleepquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.camelloncase.trackmysleepquality.R
import com.camelloncase.trackmysleepquality.database.SleepDatabase
import com.camelloncase.trackmysleepquality.database.SleepDatabaseDao
import com.camelloncase.trackmysleepquality.database.SleepNight
import com.camelloncase.trackmysleepquality.databinding.FragmentSleepQualityBinding
import com.camelloncase.trackmysleepquality.sleeptracker.SleepTrackerFragmentDirections
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    private var _binding: FragmentSleepQualityBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataSource: SleepDatabaseDao
    private lateinit var viewModel: SleepQualityViewModel
    private lateinit var viewModelFactory: SleepQualityViewModelFactory

    @InternalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_quality, container, false)

        val application = requireNotNull(this.activity).application
        dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        viewModelFactory = SleepQualityViewModelFactory(
            SleepQualityFragmentArgs.fromBundle(requireArguments()).sleepNightKey,
            dataSource
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[SleepQualityViewModel::class.java]

        binding.sleepQualityViewModel = viewModel

        viewModel.navigateToSleepTracker.observe(this,  Observer {
            if (it == true) { // Observed state is true.
                goToNextScreen()
            }
        })

        return binding.root
    }

    private fun goToNextScreen() {
        findNavController().navigate(
            SleepQualityFragmentDirections
                .actionSleepQualityFragmentToSleepTrackerFragment())
        viewModel.doneNavigating()
    }
}
