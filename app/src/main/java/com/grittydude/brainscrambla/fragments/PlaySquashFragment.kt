package com.grittydude.brainscrambla.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.databinding.FragmentPlaySquashBinding


class PlaySquashFragment : Fragment() {

    private var _binding: FragmentPlaySquashBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onResume() {
        super.onResume()

        val category = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down, category)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        val timer = resources.getStringArray(R.array.timer)
        val timerAdapter = ArrayAdapter(requireContext(), R.layout.drop_down, timer)
        binding.autoCompleteTextView2.setAdapter(timerAdapter)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaySquashBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.play.setOnClickListener {
            val cat = binding.autoCompleteTextView.text.toString()
            val time = binding.autoCompleteTextView2.text.toString()
            if (cat == "Choose your category") {
                Toast.makeText(requireContext(), "Kindly choose a category", Toast.LENGTH_SHORT)
                    .show()
            } else if (time == "Set timer") {
                binding.layout2.isErrorEnabled = true
                binding.layout2.error = "Kindly set a timer"
            } else {
                val bundle = Bundle()
                bundle.putString("cat", cat)
                bundle.putString("time", time)
                Toast.makeText(requireContext(), cat, Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_playSquashFragment_to_game, bundle)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}