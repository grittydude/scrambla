
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.grittydude.brainscrambla.R
import com.grittydude.brainscrambla.databinding.FragmentDropAFeedBackBinding


class DropAFeedBack : Fragment() {

    private lateinit var binding: FragmentDropAFeedBackBinding
    private lateinit var navController: NavController
    private lateinit var ref: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDropAFeedBackBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_dropAFeedBack_to_menuFragment)
        }

        auth = Firebase.auth
        ref = FirebaseFirestore.getInstance()

        binding.submit.setOnClickListener {

            if (binding.dropFeedback.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your text", Toast.LENGTH_SHORT)
                    .show()
            } else {
                sendFeedBack()

            }
        }
        return binding.root
    }

    private fun sendFeedBack() {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        val feedbackRef: CollectionReference = ref.collection("Feedbacks")

        val feedBack = FeedBack(userId!!, userEmail!!, binding.dropFeedback.text.toString())

        feedbackRef.add(feedBack).addOnSuccessListener {
            Toast.makeText(
                requireContext(),
                "Your feedback has been sent",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(R.id.action_dropAFeedBack_to_menuFragment)
        }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Fail to add feedback \n$e", Toast.LENGTH_SHORT)
                    .show()
            }
    }

}