package com.jonathansteele.topcointrack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CoinLoreViewModel : ViewModel() {
    private val repository = CoinLoreRepository() // Instantiate repository
    private var _allCoins: List<Coin> by mutableStateOf(emptyList())
    var sortedCoins: List<Coin> by mutableStateOf(emptyList())
    var isLoading: Boolean by mutableStateOf(false) // Loading state for initial fetch
    var isRefreshing: Boolean by mutableStateOf(false) // Loading state for pull-to-refresh

    init {
        fetchTickers()
    }

    private fun fetchTickers() {
        viewModelScope.launch {
            isLoading = true
            val coins = repository.fetchTickers()
            _allCoins = coins
            sortedCoins = coins
            isLoading = false
        }
    }

    fun fetchTickersForRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            val coins = repository.fetchTickers()
            _allCoins = coins
            sortedCoins = coins
            isRefreshing = false
        }
    }

    fun sortCoins(by: SortCriteria) {
        sortedCoins = when (by) {
            SortCriteria.DEFAULT -> _allCoins
            SortCriteria.PRICE -> _allCoins.sortedByDescending { it.priceUsd.toFloat() }
            SortCriteria.CHANGE -> _allCoins.sortedByDescending { it.percentChange24h.toFloat() }
        }
    }
}

enum class SortCriteria {
    DEFAULT, PRICE, CHANGE
}