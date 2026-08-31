package com.example.itew3_midterm_groupapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One row = one logged item (e.g. "Home keys" placed in the "Bathroom").
 *
 * This matches the "Item Info" mockup fields:
 *  - name        -> title shown on Home + Item Info ("Home Keys")
 *  - location    -> "Smart Zone" ("Living room", "Bathroom", etc.)
 *  - placedBy    -> "Placed By" ("Mika")
 *  - imageUri    -> locally-picked photo of the item
 *  - loggedAt    -> "Time Logged" (used for the "5m", "10m" ago labels)
 *  - retrieved   -> whether "Mark as Retrieved" has been pressed
 *  - retrievedAt -> when it was retrieved, used to sort the History Logs page
 */
@Entity(tableName = "item_log")
data class ItemLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val location: String,
    val placedBy: String,
    val imageUri: String? = null,
    val loggedAt: Long = System.currentTimeMillis(),
    val retrieved: Boolean = false,
    val retrievedAt: Long? = null
)
