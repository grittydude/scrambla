package com.grittydude.brainscrambla.fragments
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.databinding.FragmentRegisterFragmentBinding
import com.grittydude.brainscrambla.datasource.Constants
import com.grittydude.brainscrambla.datasource.Users

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var fStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegisterFragmentBinding.inflate(inflater, container, false)

        navController = NavHostFragment.findNavController(this)
        val view = binding.root

        auth = Firebase.auth
        fStore = FirebaseFirestore.getInstance()

        binding.iAlreadyHaveAnAccount.setOnClickListener {
            navigateTo(R.id.action_register_fragment_to_login_page_fragment)
        }
        binding.createAccount.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.USERNAME, binding.registerUserName.text.toString())
            registerUser()
        }

        binding.loginTextview.setOnClickListener {
            navigateTo(R.id.action_register_fragment_to_login_page_fragment)
        }

        return view
    }

    private fun navigateTo(id: Int) {
        navController.navigate(id)
    }

    private fun registerUser() {

        val email = binding.registerEmail.text.toString().trim { it <= ' ' }
        val password = binding.registerPassword.text.toString().trim { it <= ' ' }
        val confirmPassword = binding.registerConfirmPassword.text.toString().trim { it <= ' ' }
        val username = binding.registerUserName.text.toString().trim()



        if (email.isEmpty()) {
            showErrorSnackBar("Email cannot be left empty", true)
            binding.progressBar.visibility = View.GONE
        } else if (username.isEmpty()) {
            showErrorSnackBar("User name cannot be left empty", true)
            binding.progressBar.visibility = View.GONE
        } else if (!isValidEmail(email)) {
            showErrorSnackBar("Input a correct email", true)
            binding.progressBar.visibility = View.GONE
        } else if (!isValidPassword(password)) {
            showErrorSnackBar(
                "Password must contain a capital letter ,a small letter, a number and a symbol",
                true
            )
        } else if (password.isEmpty()) {
            showErrorSnackBar("Password cannot be left empty", true)
        } else if (binding.registerUserName.text.toString().isEmpty()) {
            showErrorSnackBar("UserName cannot be left blank", true)
        } else if (password.length < 6) {
            showErrorSnackBar("Password must be more than 6 characters", true)
            binding.progressBar.visibility = View.GONE
        } else if (password != confirmPassword) {
            showErrorSnackBar("Please fill in all the required information", true)
            binding.progressBar.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {

                        Toast("You have successfully created a account")
                        val user = FirebaseAuth.getInstance().currentUser?.uid
                        val myUser = Users(
                            binding.registerEmail.text.toString().trim { it <= ' ' },
                            binding.registerPassword.text.toString().trim { it <= ' ' },
                            binding.registerConfirmPassword.text.toString().trim { it <= ' ' },
                            binding.registerUserName.text.toString().trim()
                        )
                        if (user != null) {
                            fStore.collection("users").document(user).set(myUser)
                        }
                        binding.progressBar.visibility = View.GONE
                        successAlertDialog()


                    } else {

                        // If sign in fails, display a message to the user.
                        Toast(task.exception?.message.toString())
                        showErrorSnackBar(task.exception?.message.toString(), true)
                        failedAlertDialog()
                        binding.progressBar.visibility = View.GONE
                    }
                }
        }
    }

    private fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.snackBarFailure
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_primary_color
                )
            )
        }
        snackBar.show()
    }

    private fun successAlertDialog() {
        val v = View.inflate(requireContext(), R.layout.success_modal_dialog, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(v)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val button = v.findViewById<Button>(R.id.btn_continue)
        button.setOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_register_fragment_to_login_page_fragment)
            }
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun Toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun failedAlertDialog() {
        val v = View.inflate(requireContext(), R.layout.failed_modal_dialog, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(v)

        val dialog = builder.create()
        dialog.show()
        val button = v.findViewById<Button>(R.id.btn_failed_continue)
        button.setOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_register_fragment_to_login_page_fragment)
            }
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}