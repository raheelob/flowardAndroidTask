package com.floward.androidtask.data.local

import androidx.room.*
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserPostsData

@Dao
interface UserDao {
    /*
    * crud for user data...
    * */
    //Add a single user data...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleUserData(data: UserData)

    //Add a single user data...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUserData(list:List<UserData>)

    // deletes single user data...
    @Delete
    suspend fun deleteSingleUserData(data: UserData)

    //Get all the user data...
    @Query("Select * from UserData")
    fun getAllUserData(): List<UserData>

    //Delete all the user data...
    @Query("DELETE FROM UserData")
    fun deleteAllUserData()

    /*
   * crud for user posts...
   * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleUserPost(data: UserPostsData)

    //Add a single user post...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUserPostsData(list:List<UserPostsData>)

    // deletes single user post...
    @Delete
    suspend fun deleteSingleUserData(data: UserPostsData)

    //Get all the user post...
    @Transaction
    @Query("Select * from UserData")
    fun getAllUserPostsData(): List<UserAndTheirPostsData>

    //Delete all the user post...
    @Query("DELETE FROM UserPostsData")
    fun deleteAllUserPostsData()

}