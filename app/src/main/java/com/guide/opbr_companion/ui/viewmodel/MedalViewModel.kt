package com.guide.opbr_companion.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guide.opbr_companion.data.SupabaseClient
import com.guide.opbr_companion.data.model.FilterState
import com.guide.opbr_companion.data.model.MedalSet
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedalViewModel : ViewModel() {
    private val _medalList = MutableStateFlow<List<MedalSet>>(emptyList())
    var medalList: StateFlow<List<MedalSet>> = _medalList

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState

    // Add null safety to filter operation
    val filterMedalList: StateFlow<List<MedalSet>> =
        combine(_medalList, _filterState) { medals, filter ->
            medals.filter { medal ->
                filter.selectedTags.isEmpty() || filter.selectedTags.any { tag ->
                    medal.bestFor?.equals(tag, ignoreCase = true) == true
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTags = listOf("Attacker", "Defender", "Runner")

    init {
        viewModelScope.launch {
            fetchMedals()
        }
    }

    private suspend fun fetchMedals() {
        try {
            val medals = SupabaseClient.client
                .from("medal_set")
                .select(
                    Columns.list(
                        "id",
                        "name",
                        "medals",
                        "medal_traits",
                        "best_for",
                        "description",
                        "tags"
                    )
                )
                .decodeList<MedalSet>()

            // Filter out any invalid medals (those without an ID)
            val validMedals = medals.filter { true }
            _medalList.value = validMedals

        } catch (e: Exception) {
            Log.e("Supabase", "Error fetching medals: ${e.message}", e)
            // Keep the existing list in case of error instead of setting to empty
            // This prevents the UI from flickering if a refresh fails
        }
    }

    fun updateFilter(tags: Set<String>) {
        try {
            _filterState.value = FilterState(selectedTags = tags)
        } catch (e: Exception) {
            Log.e("MedalViewModel", "Error updating filter: ${e.message}", e)
            // Keep the existing filter in case of error
        }
    }

    fun getMedalById(id: Int): MedalSet? {
        return try {
            _medalList.value.find { it.id == id }
        } catch (e: Exception) {
            Log.e("MedalViewModel", "Error finding medal by ID: ${e.message}", e)
            null
        }
    }
}