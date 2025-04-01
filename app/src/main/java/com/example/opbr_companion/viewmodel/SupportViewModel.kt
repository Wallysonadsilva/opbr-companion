package com.example.opbr_companion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opbr_companion.data.SupabaseClient
import com.example.opbr_companion.model.FilterState
import com.example.opbr_companion.model.Support
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SupportViewModel : ViewModel() {
    private val _supportList = MutableStateFlow<List<Support>>(emptyList())
    val supportList: StateFlow<List<Support>> = _supportList

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState

    val filteredSupportList: StateFlow<List<Support>> = combine(_supportList, _filterState) { supports, filter ->
        supports.filter { support ->
            val colorMatches = filter.selectedColor?.equals(support.supportColor, ignoreCase = true) ?: true
            val tagsMatch = filter.selectedTags.all { tag -> support.supportTags.contains(tag) }
            colorMatches && tagsMatch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTags: StateFlow<Set<String>> = _supportList
        .map { list -> list.flatMap { it.supportTags }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val allColors: StateFlow<Set<String>> = _supportList
        .map { list -> list.map { it.supportColor }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        viewModelScope.launch {
            fetchSupportData()
        }
    }

    private suspend fun fetchSupportData() {
        try {
            val supportData = SupabaseClient.client
                .from("support")
                .select(Columns.list("id", "support_img", "support_color", "support_tags"))
                .decodeList<Support>()

            _supportList.value = supportData
        } catch (e: Exception) {
            // error
        }
    }

    fun updateFilter(color: String?, tags: Set<String>) {
        _filterState.value = FilterState(color, tags)
    }
}
