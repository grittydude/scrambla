package com.grittydude.brainscrambla.datasource
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.grittydude.brainscrambla.fragments.EditProfile


class FireStoreData {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun about(fragment:Fragment, userinfo: Users) {
        val db = Firebase.firestore
        db.collection(Constants.users)
            .add(userinfo)
            .addOnSuccessListener { documentReference ->
                Log.d("Me", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Me", "Error adding document", e)
            }

    }

    fun getCurrentUserID() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }


    fun updateUserInfoInFireStore(fragment: EditProfile, userHashMap: HashMap<String, Any>, context: Context) {

        mFirestore.collection("users")
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                fragment.userInfoUpdateSuccess()
            }
            .addOnFailureListener {
                Toast.makeText(context, "com.grittydude.brainscrambla.fragments.Profile Update failed", Toast.LENGTH_SHORT).show()
            }

    }




}