package com.floward.androidtask.data.local

import androidx.room.*
import com.floward.androidtask.data.response.model.UserData

@Dao
interface UserDao {
    //Add a single top story...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleUserData(data: UserData)

    //Add a single top story...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUserData(list:List<UserData>)

    // deletes single top story...
    @Delete
    suspend fun deleteSingleUserData(data: UserData)

    //Get all the top stories...
    @Query("Select * from UserData")
    fun getAllUserData(): List<UserData>

    //Delete all the top stories...
    @Query("DELETE FROM UserData")
    fun deleteAllUserData()
}