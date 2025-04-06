package com.example.opbr_companion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: Int,
    val artwork: String,
    @SerialName("class") val classType: String,
    val color: String,
    val name: String,
    val title: String?,
    val guide: String?,
    @SerialName("recommended_set") val recommendedSet: List<String>?,
    @SerialName("set_message") val setMessage: String?,
    @SerialName("recommended_stats") val recommendStats: String?,
    @SerialName("stat_message") val statMessage: String?,
    val medal: String?,
    @SerialName("medal_tags") val medalTags: List<String>?,
    @SerialName("medal_trait") val medalTrait: String?,
    @SerialName("character_tags") val characterTags: List<String>?
)

