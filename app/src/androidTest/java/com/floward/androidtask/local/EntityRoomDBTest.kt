package com.floward.androidtask.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.floward.androidtask.data.local.UserDao
import com.floward.androidtask.data.local.UserDatabase
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserPostsData
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EntityRoomDBTest {

    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase
    private lateinit var context: Context
    private lateinit var userPostList:List<UserAndTheirPostsData>

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = db.UserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUsersAndTheirPostAndReadInList() = runBlocking {
        userDao.insertAllUserData(initListUsers())//write operation...
        userDao.insertAllUserPostsData(initPostsList())
        userPostList= userDao.getAllUserPostsData()//read operation
        assertTrue(userPostList.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllUsersAndTheirPostToDb() = runBlocking {
        userDao.insertAllUserData(initListUsers())
        userDao.insertAllUserPostsData(initPostsList())
        userDao.deleteAllUserData()//delete operation...
        userPostList= userDao.getAllUserPostsData()//read operation
        assertTrue(userPostList.isEmpty())
    }

    private fun initListUsers() = object : ArrayList<UserData>() {
        init { // dummy list added to check the event...
            add(UserData())
            add(UserData())
            add(UserData())
        }
    }

    private fun initPostsList() = object : ArrayList<UserPostsData>() {
        init { // dummy list added to check the event...
            add(UserPostsData())
            add(UserPostsData())
            add(UserPostsData())
        }
    }

}





