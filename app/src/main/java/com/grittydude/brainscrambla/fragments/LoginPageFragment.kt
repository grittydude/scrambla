package com.grittydude.brainscrambla.fragments

import com.grittydude.brainscrambla.activities.LandingPage
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.databinding.FragmentLoginPageFragmentBinding
import com.grittydude.brainscrambla.datasource.Constants

class LoginPageFragment : Fragment() {

    private lateinit var binding: FragmentLoginPageFragmentBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "GOOGLE-AUTH"
    //private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var navController: NavController
    private lateinit var gso: GoogleSignInOptions


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentLoginPageFragmentBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)


        val view = binding.root

        auth = Firebase.auth

//        val currentUser = auth.currentUser
//        currentUser!!.displayName
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.GoogleSignIn)
            .requestEmail()
            .build()

        //mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)


        binding.linear2.setOnClickListener {
            googleSignIn()

        }

        binding.tvForgotPassword.setOnClickListener {

            Navigation.findNavController(view)
                .navigate(R.id.action_login_page_fragment_to_forgot_password_fragment3)
        }

        binding.loginBtn.setOnClickListener {
            binding.progressBar2.visibility = View.VISIBLE
            loginUser()

        }

        binding.DontHaveAnAccount.setOnClickListener {

            navController.navigate(R.id.action_login_page_fragment_to_register_fragment)
        }

        binding.signUp.setOnClickListener {
            navController.navigate(R.id.action_login_page_fragment_to_register_fragment)
        }

        return view

    }

    private fun googleSignIn() {
        //val signInIntent = mGoogleSignInClient.signInIntent
        //startActivityForResult(signInIntent, com.grittydude.brainscrambla.datasource.Constants.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            if (task.isSuccessful) {
//                try {
//                    val account = task.getResult(ApiException::class.java)
//                    googleAuthForFirebase(account.idToken!!)
//                } catch (e: ApiException) {
//
//                }
//            }
        }
    }

    private fun loginUser() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        if (email.isEmpty()) {
            showErrorSnackBar("Email cannot be left blank", true)
            binding.progressBar2.visibility = View.GONE
        } else if (password.isEmpty()) {
            showErrorSnackBar("Password cannot be left blank", true)
            binding.progressBar2.visibility = View.GONE
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        showErrorSnackBar("Login Successful", false)
                        val intent = Intent(requireContext(), LandingPage::class.java)
                        activity?.startActivity(intent)
                        Toast("You have successfully logged in")
                        binding.progressBar2.visibility = View.GONE
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast("Authentication Failed")
                        binding.progressBar2.visibility = View.GONE
                    }
                }
        }
    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        val snackbarView = snackBar.view

        if (errorMessage) {
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.snackBarFailure
                )
            )
        } else {
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_primary_color
                )
            )
        }
        snackBar.show()
    }


    fun Toast(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT)
            .show()
    }


    private fun googleAuthForFirebase(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showErrorSnackBar("Login Successful", false)
                    if (user != null) {

                        val intent = Intent(requireContext(), LandingPage::class.java)
                        activity?.startActivity(intent)
                    }
                } else {
                    showErrorSnackBar("Login Unsuccessful", true)

                    android.widget.Toast.makeText(
                        requireContext(),
                        "Operation failed",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }

}