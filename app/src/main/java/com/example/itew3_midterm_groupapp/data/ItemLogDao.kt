package com.example.itew3_midterm_groupapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemLogDao {

    // Home Page: "Currently Logged Items" (not yet retrieved), newest first
    @Query("SELECT * FROM item_log WHERE retrieved = 0 ORDER BY loggedAt DESC")
    fun getActiveItems(): Flow<List<ItemLog>>

    // Home Page search textfield: filter by item name OR location
    @Query(
        "SELECT * FROM item_log WHERE retrieved = 0 " +
        "AND (name LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%') " +
        "ORDER BY loggedAt DESC"
    )
    fun searchActiveItems(query: String): Flow<List<ItemLog>>

    // History Logs Page: items that have been marked as retrieved
    @Query("SELECT * FROM item_log WHERE retrieved = 1 ORDER BY retrievedAt DESC")
    fun getHistoryItems(): Flow<List<ItemLog>>

    // Item Info Page: load one item by id
    @Query("SELECT * FROM item_log WHERE id = :itemId")
    fun getItemById(itemId: Int): Flow<ItemLog?>

    @Query("SELECT * FROM item_log ORDER BY loggedAt DESC")
    fun getAllItems(): Flow<List<ItemLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemLog): Long

    @Update
    suspend fun update(item: ItemLog)

    @Delete
    suspend fun delete(item: ItemLog)
}
