package com.jonathansteele.topcointrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CoinDao {
    @Query("DELETE FROM coins")
    suspend fun clearCoins()

    @Query("SELECT * FROM coins")
    fun getAllCoins(): List<CoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoins(coins: List<CoinEntity>)

    @Transaction
    suspend fun clearAndInsertCoins(coins: List<CoinEntity>) {
        clearCoins() // Clear existing data
        insertCoins(coins) // Insert new data
    }
}
