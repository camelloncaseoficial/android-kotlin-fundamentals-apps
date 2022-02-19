package com.camelloncase.trackmysleepquality.sleeptracker

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
import com.camelloncase.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    private var _binding: FragmentSleepTrackerBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataSource: SleepDatabaseDao
    private lateinit var viewModel: SleepTrackerViewModel
    private lateinit var viewModelFactory: SleepTrackerViewModelFactory

    @InternalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application
        dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SleepTrackerViewModel::class.java]
        binding.sleepTrackerViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToSleepQuality.observe(this, Observer{ night ->
            night?.let {
                goToNextScreen(night)
            }
        })

        viewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                showSnackBar()
            }
        })

        return binding.root
    }

    private fun goToNextScreen(night: SleepNight) {
        findNavController().navigate(
            SleepTrackerFragmentDirections
                .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
        viewModel.doneNavigating()
    }

    private fun showSnackBar() {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            getString(R.string.cleared_message),
            Snackbar.LENGTH_SHORT // How long to display the message.
        ).show()
        viewModel.doneShowingSnackbar()
    }
}