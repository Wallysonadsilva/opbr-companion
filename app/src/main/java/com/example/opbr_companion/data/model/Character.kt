package com.example.opbr_companion.data.model

data class Character(
    val id: Int,
    val artwork: String,
    val classType: String,
    val color: String,
    val name: String,
    val title: String?,
    val guide: String?,
    val recommendedSet: String?,
    val setMessage: String?,
    val recommendStats: String?,
    val statMessage: String?,
    val medal: String?,
    val medalTags: String?,
    val medalTrait: String?,
    val characterTags: String?
)
