package com.guide.opbr_companion.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guide.opbr_companion.data.SupabaseClient
import com.guide.opbr_companion.data.model.FilterState
import com.guide.opbr_companion.data.model.Support
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
            // Add null safety to color matching
            val colorMatches = filter.selectedColor?.let {
                support.supportColor?.equals(it, ignoreCase = true)
            } ?: true

            // Add null safety to tag matching
            val tagsMatch = if (filter.selectedTags.isEmpty()) {
                true
            } else {
                support.supportTags?.let { tags ->
                    filter.selectedTags.all { tag -> tags.contains(tag) }
                } ?: false
            }

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

            // Filter out any invalid supports (those without an ID)
            val validSupports = supportData.filter { true }
            _supportList.value = validSupports

        } catch (e: Exception) {
            Log.e("SupportViewModel", "Error fetching support data: ${e.message}", e)
            // Keep existing list on error instead of setting to empty
        }
    }

    fun updateFilter(color: String?, tags: Set<String>) {
        try {
            _filterState.value = FilterState(color, tags)
        } catch (e: Exception) {
            Log.e("SupportViewModel", "Error updating filter: ${e.message}", e)
            // Keep existing filter on error
        }
    }

    fun getSupportById(id: Int): Support? {
        return try {
            _supportList.value.find { it.id == id }
        } catch (e: Exception) {
            Log.e("SupportViewModel", "Error finding support by ID: ${e.message}", e)
            null
        }
    }
}