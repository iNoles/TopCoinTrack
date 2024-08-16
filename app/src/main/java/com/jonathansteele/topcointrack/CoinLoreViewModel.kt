package com.jonathansteele.topcointrack

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fuel.Fuel
import fuel.get
import fuel.serialization.toJson
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CoinLoreViewModel : ViewModel() {
    private var _allCoins: List<Coin> by mutableStateOf(emptyList())
    var sortedCoins: List<Coin> by mutableStateOf(emptyList())
    var isLoading: Boolean by mutableStateOf(false) // Loading state

    init {
        fetchTickers()
    }

    private fun fetchTickers() {
        viewModelScope.launch {
            isLoading = true // Start loading
            val json = Json { ignoreUnknownKeys = true }
            val response = Fuel.get("https://api.coinlore.net/api/tickers/")
                .toJson(json = json, deserializationStrategy = CoinLoreResponse.serializer())
            response.fold({
                _allCoins = it?.data ?: emptyList()
                sortedCoins = _allCoins
                isLoading = false // Stop loading
            }, {
                Log.e("CoinLoreViewModel", "An unexpected error occurred.", it)
                isLoading = false // Stop loading
            })
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