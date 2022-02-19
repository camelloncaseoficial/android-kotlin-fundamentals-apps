package com.camelloncase.trackmysleepquality.sleepdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.camelloncase.trackmysleepquality.R
import com.camelloncase.trackmysleepquality.database.SleepDatabase
import com.camelloncase.trackmysleepquality.database.SleepDatabaseDao
import com.camelloncase.trackmysleepquality.databinding.FragmentSleepDetailBinding
import com.camelloncase.trackmysleepquality.sleeptracker.SleepTrackerViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.Observer

class SleepDetailFragment : Fragment() {

    private var _binding: FragmentSleepDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataSource: SleepDatabaseDao
    private lateinit var viewModel: SleepDetailViewModel
    private lateinit var viewModelFactory: SleepDetailViewModelFactory

    @InternalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_detail, container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        viewModelFactory = SleepDetailViewModelFactory(
            SleepDetailFragmentArgs.fromBundle(requireArguments()).sleepNightKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel = ViewModelProvider(
                this, viewModelFactory)[SleepDetailViewModel::class.java]

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.sleepDetailViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {// Observed state is true.
                findNavController().navigate(
                    SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment())
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }
}