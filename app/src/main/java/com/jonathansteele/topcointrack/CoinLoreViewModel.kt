package com.jonathansteele.topcointrack

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CoinLoreViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val repository: CoinLoreRepository =
        CoinLoreRepository(
            AppDatabase.getDatabase(application).coinDao(),
        )
    var sortedCoins by mutableStateOf(emptyList<CoinEntity>())
    var isLoading: Boolean by mutableStateOf(false) // Loading state for initial fetch
    var isRefreshing: Boolean by mutableStateOf(false) // Loading state for pull-to-refresh

    init {
        fetchCoins()
    }

    private fun fetchCoins() {
        viewModelScope.launch {
            isLoading = true
            try {
                sortedCoins = repository.getCoins(getApplication())
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchTickersForRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                sortedCoins = repository.getCoins(getApplication())
            } finally {
                isRefreshing = false
            }
        }
    }

    fun sortCoins(by: SortCriteria) {
        sortedCoins =
            when (by) {
                SortCriteria.DEFAULT -> sortedCoins
                SortCriteria.PRICE -> sortedCoins.sortedByDescending { it.priceUsd.toFloat() }
                SortCriteria.CHANGE -> sortedCoins.sortedByDescending { it.percentChange24h.toFloat() }
            }
    }
}

enum class SortCriteria {
    DEFAULT,
    PRICE,
    CHANGE,
}
