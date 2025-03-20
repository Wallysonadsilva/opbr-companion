package com.example.opbr_companion.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opbr_companion.data.SupabaseClient
import com.example.opbr_companion.model.Support
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SupportViewModel : ViewModel() {
    private val _supportList = MutableStateFlow<List<Support>>(emptyList())
    val supportList: StateFlow<List<Support>> = _supportList

    init {
        viewModelScope.launch {
            fetchSupportData()
        }
    }

    private suspend fun fetchSupportData() {
        try{
            val supportData = SupabaseClient.client
                .from("support")
                .select(Columns.list("id","support_img", "support_color", "support_tags"))
                .decodeList<Support>()

            _supportList.value = supportData
            Log.d("Supabase", "Fetched: $supportData")
        } catch (e: Exception) {
            Log.e("Supabase", "Error fetching data: ${e.message}")
        }
    }
}