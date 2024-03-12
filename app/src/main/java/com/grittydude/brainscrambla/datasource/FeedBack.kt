
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedBack(
    val userId: String = "",
    val userEmail: String = "",
    val feedback: String = ""
): Parcelable