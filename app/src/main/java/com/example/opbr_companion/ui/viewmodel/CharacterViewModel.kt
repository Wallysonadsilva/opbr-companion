package com.example.opbr_companion.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opbr_companion.data.SupabaseClient
import com.example.opbr_companion.data.model.Character
import com.example.opbr_companion.data.model.FilterState
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {

    private val _characterList = MutableStateFlow<List<Character>>(emptyList())
    var characterList: StateFlow<List<Character>> = _characterList

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState

    val filteredCharecterList: StateFlow<List<Character>> =
        combine(_characterList, _filterState) { characters, filter ->
            characters.filter { character ->
                filter.selectedTags.isEmpty() || filter.selectedTags.contains(character.classType)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTags = listOf("Attacker", "Defender", "Runner")

    init {
        viewModelScope.launch {
            fetchCharacters()
        }
    }

    private suspend fun fetchCharacters() {
        try {
            val characters = SupabaseClient.client
                .from("character")
                .select(
                    Columns.list(
                        "id",
                        "artwork",
                        "class",
                        "color",
                        "name",
                        "title",
                        "guide",
                        "recommended_set",
                        "set_message",
                        "recommended_stats",
                        "stat_message",
                        "medal",
                        "medal_tags",
                        "medal_trait",
                        "character_tags"
                    )
                )
                .decodeList<Character>()

            _characterList.value = characters

        } catch (e: Exception) {
            Log.e("Supabase", "Error fetching characters: ${e.message}")
        }
    }

    fun updateFilter(tags: Set<String>, color: String?) {
        _filterState.value = FilterState(selectedTags = tags, selectedColor = color)
    }

    fun getCharacterById(id: Int): Character? {
        return _characterList.value.find { it.id == id }
    }

}