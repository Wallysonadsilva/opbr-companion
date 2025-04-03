package com.example.opbr_companion.data.model

data class FilterState(
    val selectedColor: String? = null,
    val selectedTags: Set<String> = emptySet()
)
