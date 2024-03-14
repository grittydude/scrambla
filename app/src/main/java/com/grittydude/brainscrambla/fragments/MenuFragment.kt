package com.grittydude.brainscrambla.fragments
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.activities.AuthScreenActivity
import com.grittydude.brainscrambla.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {


    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linearProfile.setOnClickListener {
            navigateTo(R.id.action_menuFragment_to_profile)
        }
        binding.linearPrivacyPolicy.setOnClickListener {
            navigateTo(R.id.action_menuFragment_to_privacy_policy)
        }

        binding.linearDropFeedback.setOnClickListener {

            navigateTo(R.id.action_menuFragment_to_dropAFeedBack)

        }

        binding.linearLogin.setOnClickListener {
            logOut()
        }
        binding.linearShare.setOnClickListener {

            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Word Squash is a word puzzle application. Download now using this link https://bit.ly/3TJjSa3")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }

        binding.rateUs.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateTo(id:Int){
        navController.navigate(id)
    }

    fun logOut(){
        val v = View.inflate(requireContext(), R.layout.logout_modal, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(v)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val cancel = v.findViewById<AppCompatButton>(R.id.cancel)
        val logout = v.findViewById<TextView>(R.id.logOut)

        logout.setOnClickListener{
            auth.signOut()
            val intent = Intent(requireContext(), AuthScreenActivity::class.java)
            activity?.startActivity(intent)
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}