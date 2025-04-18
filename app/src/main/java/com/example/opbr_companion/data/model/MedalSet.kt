package com.example.opbr_companion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedalSet(
    val id: Int,
    val name: String,
    val medals: List<String> = emptyList(),
    @SerialName("medal_traits") val medalTraits: List<String> = emptyList(),
    @SerialName("best_for") val bestFor: String?,
    val description: String?,
    val tags: List<String> = emptyList()
)
