package ru.drrey.babyname.names.domain.entity

import com.google.firebase.firestore.PropertyName
import ru.drrey.babyname.names.api.Sex

data class Name(
    val displayName: String = "",
    val description: String = "",
    @PropertyName("sex") val sexString: String = "М",
    var stars: Int? = null
) {
    val sex: Sex
        get() = if (sexString == "М") Sex.BOY else Sex.GIRL
}