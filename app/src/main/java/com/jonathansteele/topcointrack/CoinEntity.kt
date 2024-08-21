package com.jonathansteele.topcointrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val percentChange24h: String
)
