package com.jonathansteele.topcointrack

import android.util.Log
import fuel.Fuel
import fuel.get
import fuel.serialization.toJson
import kotlinx.serialization.json.Json

class CoinLoreRepository {
    suspend fun fetchTickers(): List<Coin> {
        val json = Json { ignoreUnknownKeys = true }
        val response = Fuel.get("https://api.coinlore.net/api/tickers/")
            .toJson(json = json, deserializationStrategy = CoinLoreResponse.serializer())
        return response.fold(
            success = { it?.data ?: emptyList() },
            failure = {
                Log.e("CoinLoreRepository", "Failed to fetch tickers.", it)
                emptyList()
            }
        )
    }
}