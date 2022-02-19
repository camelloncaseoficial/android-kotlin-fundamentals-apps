package com.camelloncase.trackmysleepquality.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
    private lateinit var adapter: SleepNightAdapter

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
        adapter = SleepNightAdapter(SleepNightListener {
                nightId -> viewModel.onSleepNightClicked(nightId)
        })

        binding.sleepTrackerViewModel = viewModel

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
        binding.sleepList.layoutManager = manager

        binding.lifecycleOwner = viewLifecycleOwner
        binding.sleepList.adapter = adapter

        viewModel.navigateToSleepDataQuality.observe(this, Observer { night ->
            night?.let {
                goToDetailScreen(night)
            }
        })

        viewModel.navigateToSleepQuality.observe(this, Observer{ night ->
            night?.let {
                goToQualityScreen(night)
            }
        })

        viewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                showSnackBar()
            }
        })

        viewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        return binding.root
    }

    private fun goToQualityScreen(night: SleepNight) {
        findNavController().navigate(
            SleepTrackerFragmentDirections
                .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
        viewModel.doneNavigating()
    }

    private fun goToDetailScreen(night: Long) {
        findNavController().navigate(
            SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepDetailFragment(night)
        )
        viewModel.onSleepDataQualityNavigated()
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