package com.example.opbr_companion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Support(
    val id: Int,

    @SerialName("support_img")
    val supportImg: String,

    @SerialName("support_color")
    val supportColor: String,

    @SerialName("support_tags")
    val supportTags: List<String>
)
