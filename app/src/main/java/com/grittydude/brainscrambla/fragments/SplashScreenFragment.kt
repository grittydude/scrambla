
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.squash.activities.LandingPage
import com.example.squash.R
import com.example.squash.datasource.Constants
import com.google.firebase.auth.FirebaseAuth

class SplashScreenFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = NavHostFragment.findNavController(this)

        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

//        val splashScreenImage = view.findViewById<ImageView>(R.id.splashScreenImage)
//            splashScreenImage.alpha = 0f
//
//            splashScreenImage.animate().setDuration(1500).alpha(1f).withEndAction {
//                if (checkOnBoardingState() && CheckLoggedInState() ){
//                    val intent = Intent(requireContext(), LandingPage::class.java)
//                    activity?.startActivity(intent)
//                }else if (checkOnBoardingState()){
//                  navigateTo(R.id.action_splashScreenFragment_to_authScreens)
//                }else{
//                    navigateTo(R.id.action_splashScreenFragment_to_onBoardingScreen)
//                }
//                activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            }

        navigateTo(R.id.action_splashScreenFragment_to_onBoardingScreen)
        return view
    }

    private fun checkOnBoardingState(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(Constants.Finished, false)

    }

    private fun CheckLoggedInState(): Boolean{
        return auth.currentUser != null
    }

    private fun navigateTo(id: Int){
        navController.navigate(id)
    }
}