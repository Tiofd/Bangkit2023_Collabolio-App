package com.bangkit.collabolio.utilities

data class UserSwipe(
    val profile: Map<String,Any>? = null,
    val uid: String? = "",
    val email: String? = ""
)

data class Favorite(
    val profile: Map<String, Any>? = null,
    val uid: String? = "",
    val email: String? = ""
)

data class UserApi(
    val similarity_score: Double? = null,
    val uid: String? = ""
)

data class Profile(
    val skill: List<Skill>
)

data class Skill(
    val name: String
)