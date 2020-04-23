package ru.drrey.babyname.names.domain.entity

data class Name(
    val displayName: String = "",
    val description: String = "",
    val sex: String = "М",
    var stars: Int? = null
)