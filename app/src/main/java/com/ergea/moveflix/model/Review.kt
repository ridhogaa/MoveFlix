package com.ergea.moveflix.model


data class Review(
    val author: String? = null,
    val authorDetails: AuthorDetails? = null,
    val content: String? = null,
    val createdAt: String? = null,
    val id: String? = null,
    val updatedAt: String? = null,
    val url: String? = null
) {
    data class AuthorDetails(
        val name: String? = null,
        val username: String? = null,
        val avatarPath: String? = null,
        val rating: Double? = null
    )
}
