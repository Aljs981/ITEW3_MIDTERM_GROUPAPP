package com.example.itew3_midterm_groupapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ItemLog::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemLogDao(): ItemLogDao

    companion object {
        // Volatile so writes to this field are visible to all threads immediately.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Double-checked locking: reuse the same instance instead of opening
            // a new SQLite connection every time getDatabase() is called.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sulyap_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
