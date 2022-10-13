package com.floward.androidtask.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserPostsData

@Database(entities = [UserData::class, UserPostsData::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase(){
    abstract fun UserDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE : UserDatabase? = null
        private const val DB_NAME = "user_database.db"
        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}