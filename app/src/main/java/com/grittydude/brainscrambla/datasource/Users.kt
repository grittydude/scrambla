
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users (
    val email: String="",
    val password: String="",
    val confirmPassword: String="",
    val username: String="",
    val city: String ="",
    val about: String="",

): Parcelable