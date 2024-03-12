
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.squash.R
import com.example.squash.databinding.FragmentProfileBinding
import com.example.squash.datasource.PreferenceManager
import com.example.squash.datasource.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class Profile : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfileBinding
    var preferenceManager: PreferenceManager? = null
    private var db: FirebaseFirestore? = null
    private var userId: String? = null
    private lateinit var auth: FirebaseAuth
    private var imageRef = Firebase.storage.reference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        preferenceManager?.preferenceManager(requireContext())
        navController = NavHostFragment.findNavController(this)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser?.email


        if (user != null) {
            downloadImage(user)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()
        if (currentUser != null) {
            userId = currentUser.uid
        }

        val docRef = db?.collection("users")?.document(userId!!)

        docRef?.get()?.addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(Users::class.java)
            binding.profileEmail.text = " ${user?.email}"
            binding.profileName.text = " ${user?.username}"
            binding.about.text = " ${user?.about}"


            binding.backBtn.setOnClickListener {
                navController.navigate(R.id.action_profile_to_menuFragment)
            }
            binding.editProfileBTN.setOnClickListener {
                val action = ProfileDirections.actionProfileToEditProfile(user!!)
                navController.navigate(action)
            }
        }
        return binding.root
    }

    private fun downloadImage(filename: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = imageRef.child("images/$filename").getBytes(maxDownloadSize).await()
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            withContext(Dispatchers.Main) {
                binding.imageProfile.setImageBitmap(bmp)
            }

        } catch (e: Exception) {

        }
    }

}