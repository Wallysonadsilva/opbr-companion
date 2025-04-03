package com.example.opbr_companion.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opbr_companion.data.SupabaseClient
import com.example.opbr_companion.data.model.FilterState
import com.example.opbr_companion.data.model.Support
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

    val allTags = listOf(
        "Attacker", "Defender", "Runner", "East Blue", "Navy",
        "The Seven Warlords of the Sea", "Straw Hat Pirates", "Whitebeard Pirates",
        "Don Quixote Family", "Paramecia", "Zoan", "Logia", "Captain",
        "The Grand Line", "New World", "Worst Generation", "Charlotte Family",
        "Kozuki Clan", "Kozuki Clan Servant", "Animal Kingdom Pirates",
        "Revolutionary Army", "Roger Pirates", "Ex-Roger Pirates", "Fish-Man"
    )

    val allColors = listOf("Red", "Green", "Blue", "Light", "Dark")

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
            Log.d("Error:", "Unable to fetch Support data")
        }
    }

    fun updateFilter(color: String?, tags: Set<String>) {
        _filterState.value = FilterState(color, tags)
    }
}
