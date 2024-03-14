package com.grittydude.brainscrambla.fragments
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.databinding.FragmentEditProfileBinding
import com.grittydude.brainscrambla.datasource.FireStoreData
import com.grittydude.brainscrambla.datasource.PreferenceManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*


class EditProfile : Fragment() {

    private lateinit var navController: NavController
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStoreData: FirebaseFirestore
    val requestCode = 0
    private lateinit var binding: FragmentEditProfileBinding
    private var preferenceManager: PreferenceManager? = PreferenceManager()
    private var imageRef = Firebase.storage.reference
    private val args by navArgs<EditProfileArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        preferenceManager?.preferenceManager(requireContext())
        auth = FirebaseAuth.getInstance()
        fireStoreData = FirebaseFirestore.getInstance()


        navController = NavHostFragment.findNavController(this)


        binding.username.setText(args.user.username)
        binding.tellUs.setText(args.user.about)
        binding.city.setText(args.user.city)

        val user = auth.currentUser?.email
        if (user != null) {
            downloadImage(user)
        }



        if (auth.currentUser != null) {
            binding.editProfEmail.setText(auth.currentUser!!.email)
        }

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_edit_Profile_to_profile)
        }

        binding.textAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, requestCode)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val user = auth.currentUser?.email
        binding.saveChanges.setOnClickListener {
            updateUserProfileDetails()
            if (user != null) {
                uploadToFireBaseStorage(user)
            }
            checkDetails()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val imageProfile = view?.findViewById<CircleImageView>(R.id.imageProfile)

            imageUri = data.data
            val bitmap = imageUri?.let { uriToBitMap(it) }
            imageProfile?.setImageBitmap(bitmap)
        }
    }

    private fun uploadToFireBaseStorage(filename: String)  {
        try {
            imageUri?.let {
                imageRef.child("images/$filename").putFile(it)
            }

        } catch (e: Exception) {


        }

    }

        private fun downloadImage(filename: String)= CoroutineScope(Dispatchers.IO).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = imageRef.child("images/$filename").getBytes(maxDownloadSize).await()
            val bmp = BitmapFactory.decodeByteArray(bytes, 0,bytes.size)
            withContext(Dispatchers.Main){
                binding.imageProfile.setImageBitmap(bmp)
            }

        } catch (e: Exception) {

        }
    }

     fun updateUserProfileDetails(){

        val userHashMap = HashMap<String, Any>()
        val email = binding.editProfEmail.text.toString().trim { it <= ' ' }
        if (email != args.user.email){
            userHashMap["email"] = email
        }
        val username = binding.username.text.toString().trim { it <= ' ' }
        if (username != args.user.username){
            userHashMap["username"] = username
        }

        val aboutUs = binding.tellUs.text.toString().trim { it <= ' ' }
        if (aboutUs != args.user.about){
            userHashMap["about"] = aboutUs
        }

        val city = binding.city.text.toString().trim { it <= ' ' }
        if (city != args.user.city){
            userHashMap["city"] = city
        }


        FireStoreData().updateUserInfoInFireStore(this, userHashMap, requireContext())

    }

    private fun checkDetails(){
        if (binding.username.text.toString().isEmpty()){
            showErrorSnackBar("Username must not be left empty", true)
        }else if(binding.city.text.toString().isEmpty()){
            showErrorSnackBar("City cannot be left empty", true)
            binding.layout3.error = "Please input this field"
        }else if (binding.tellUs.text.toString().isEmpty()){
            showErrorSnackBar("Details should not be left empty", true)
        }else{
            navController.navigate(R.id.action_edit_Profile_to_profile)
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




    fun userInfoUpdateSuccess(){
        Toast.makeText(requireContext(), "com.grittydude.brainscrambla.fragments.Profile updated successfully", Toast.LENGTH_SHORT).show()
    }

    private fun uriToBitMap(selectedFileUri: Uri): Bitmap? {

        try {
            val parcelFileDescriptor =
                context?.contentResolver?.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            return BitmapFactory.decodeFileDescriptor(fileDescriptor)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}

