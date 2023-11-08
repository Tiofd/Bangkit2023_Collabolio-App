package com.bangkit.collabolio.response

data class Profile(
    val bio: String = "",
    val birthDate: String = "",
    val displayName: String = "",
    val educations: Education = Education(),
    val male: Boolean? = null,
    val location: String = "",
    val phoneNumber: String = "",
    val photoURL: String = "",
    val skills: List<Skills> = listOf()
)