package com.bangkit.collabolio.ui

import com.bangkit.collabolio.response.Connections
import com.bangkit.collabolio.response.Education
import com.bangkit.collabolio.response.Interests
import com.bangkit.collabolio.response.Skills

data class Users(
    val profile: UserProfile?
)

data class UserProfile(
    val displayName: String,
    val phoneNumber: String,
    val birthDate: String,
    val male: Boolean,
    val Location: String,
    val educations: Education,
    val bio: String,
    val skills : List<Skills>,
    val interests: List<Interests>,
    val connections: List<Connections>
)