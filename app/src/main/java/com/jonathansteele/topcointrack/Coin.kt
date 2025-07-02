package com.jonathansteele.topcointrack

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinLoreResponse(
    val data: List<Coin>,
)

@Serializable
data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    @SerialName("price_usd") val priceUsd: String,
    @SerialName("percent_change_24h") val percentChange24h: String,
)
