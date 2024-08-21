package com.jonathansteele.topcointrack

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import fuel.Fuel
import fuel.get
import fuel.serialization.toJson
import kotlinx.serialization.json.Json

class CoinLoreRepository(private val coinDao: CoinDao) {
    suspend fun getCoins(context: Context): List<CoinEntity> {
        return if (isNetworkAvailable(context)) {
            val coins = fetchCoinsFromApi()
            // Replace old data with new data
            coinDao.clearAndInsertCoins(coins)
            coins
        } else {
            // Fetch cached coins from the local database
            coinDao.getAllCoins()
        }
    }

    private suspend fun fetchCoinsFromApi(): List<CoinEntity> {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            val response = Fuel.get("https://api.coinlore.net/api/tickers/")
                .toJson(json = json, deserializationStrategy = CoinLoreResponse.serializer())

            response.fold(
                success = { coinResponse ->
                    // Map API data to CoinEntity
                    coinResponse?.data?.map { coin ->
                        CoinEntity(
                            id = coin.id,
                            name = coin.name,
                            symbol = coin.symbol,
                            priceUsd = coin.priceUsd,
                            percentChange24h = coin.percentChange24h
                        )
                    } ?: emptyList()
                },
                failure = {
                    Log.e("CoinRepository", "Error fetching coins from API", it)
                    emptyList() // Return an empty list in case of an error
                }
            )
        } catch (e: Exception) {
            Log.e("CoinRepository", "An unexpected error occurred", e)
            emptyList() // Return an empty list in case of an exception
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
