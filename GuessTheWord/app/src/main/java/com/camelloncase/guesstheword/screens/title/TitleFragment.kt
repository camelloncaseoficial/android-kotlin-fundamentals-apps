package com.camelloncase.guesstheword.screens.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.camelloncase.guesstheword.R
import com.camelloncase.guesstheword.databinding.FragmentTitleBinding

/**
 * Fragment for the starting or title screen of the app
 */
class TitleFragment : Fragment() {

    private var _binding: FragmentTitleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_title, container, false)

        val view = binding.root

        binding.playGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_title_to_game)
        }

        return view
    }
}