package com.example.opbr_companion.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opbr_companion.data.SupabaseClient
import com.example.opbr_companion.data.model.FilterState
import com.example.opbr_companion.data.model.MedalSet
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

    val filterMedalList: StateFlow<List<MedalSet>> =
        combine(_medalList, _filterState) { medals, filter ->
            medals.filter { medal ->
                filter.selectedTags.isEmpty() || filter.selectedTags.any {
                    it.equals(
                        medal.bestFor,
                        ignoreCase = true
                    )
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

            _medalList.value = medals
        } catch (e: Exception) {
            Log.e("Supabase", "Error fetching medals: ${e.message}")
        }
    }

    fun updateFilter(tags: Set<String>) {
        _filterState.value = FilterState(selectedTags = tags)
    }

}