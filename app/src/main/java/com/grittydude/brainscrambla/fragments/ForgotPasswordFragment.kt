package com.grittydude.brainscrambla.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.grittydude.brainscrambla.databinding.FragmentForgotPasswordFragmentBinding


class ForgotPasswordFragment : Fragment() {

    private lateinit var binding : FragmentForgotPasswordFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordFragmentBinding.inflate(inflater, container, false)

        val view = binding.root

        auth = Firebase.auth

        binding.submit.setOnClickListener {

            val forgotPasswordEmail = binding.forgotPasswordEmail.text.toString().trim { it <= ' '}

            if (forgotPasswordEmail.isEmpty()){
                Toast.makeText(requireContext(), "Email cannot be left blank", Toast.LENGTH_SHORT).show()
            }else{

                auth.sendPasswordResetEmail(forgotPasswordEmail)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                            Toast.makeText(requireContext(), "Email sent", Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(requireContext(), task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }

        return view
    }

}