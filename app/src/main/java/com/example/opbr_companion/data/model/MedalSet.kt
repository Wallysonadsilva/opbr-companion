package com.example.opbr_companion.data.model

data class MedalSet(
    val id: Int,
    val name: String,
    val medals: String?,
    val medalTraits: String?,
    val bestFor: String?,
    val description: String?,
    val tags: String?
)
