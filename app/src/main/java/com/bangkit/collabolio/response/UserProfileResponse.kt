package com.bangkit.collabolio.response

data class UserProfileResponse(
    val email: String = "",
    val profile: Profile = Profile(),
    val uid: String = "",
    val connections : List<Connections> = listOf()
)