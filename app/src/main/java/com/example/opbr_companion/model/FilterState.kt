package com.example.opbr_companion.model

data class FilterState(
    val selectedColor: String? = null,
    val selectedTags: Set<String> = emptySet()
)
