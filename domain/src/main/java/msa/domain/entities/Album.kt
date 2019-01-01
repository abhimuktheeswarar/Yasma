package msa.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: Int,
    val title: String,
    val userId: Int
)