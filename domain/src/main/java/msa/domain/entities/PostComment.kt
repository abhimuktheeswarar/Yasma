package msa.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class PostComment(
    val body: String,
    val email: String,
    val id: Int,
    val name: String,
    val postId: Int
)