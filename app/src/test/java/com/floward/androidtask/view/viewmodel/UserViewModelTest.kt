package com.floward.androidtask.view.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.egymcodingchallenge.CoroutineTestRule
import com.floward.androidtask.data.api.APIsCollection
import com.floward.androidtask.data.events.PostDataEvent
import com.floward.androidtask.data.events.UserAndTheirPostEvent
import com.floward.androidtask.data.events.UserDataEvent
import com.floward.androidtask.data.local.UserDao
import com.floward.androidtask.data.local.UserDatabase
import com.floward.androidtask.data.repository.UserRepository
import com.floward.androidtask.data.repository.UserRepositoryImpl
import com.floward.androidtask.data.response.model.ErrorData
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.view.usecase.UserAndTheirPostsUseCase
import com.floward.androidtask.view.usecase.UserPostsUseCase
import com.floward.androidtask.view.usecase.UserUseCase
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class UserViewModelTest : TestCase() {

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
    private val apiService = mock<APIsCollection>()
    private lateinit var userViewModel: UserViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var userUseCase: UserUseCase
    private lateinit var userPostUseCase: UserPostsUseCase
    private lateinit var userAndTheirPostsUseCase: UserAndTheirPostsUseCase
    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase
    private lateinit var context: Context

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    public override fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = db.UserDao()
        userRepository = UserRepositoryImpl(apiService, userDao)
        userUseCase = UserUseCase(userRepository)
        userPostUseCase = UserPostsUseCase(userRepository)
        userAndTheirPostsUseCase = UserAndTheirPostsUseCase(userRepository)
        userViewModel = UserViewModel(userUseCase, userPostUseCase, userAndTheirPostsUseCase)
    }

    @Test
    fun testPostErrorEvent() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            userViewModel.postsTasksEvent.collect {
                when (it) {
                    is PostDataEvent.Error -> {
                        it.errorData.error?.let { errorStr -> assertTrue(errorStr.isNotEmpty()) }
                    }
                    is PostDataEvent.Loading ->{}
                    is PostDataEvent.GetPosts ->{}
                    is PostDataEvent.RemoteErrorByNetwork ->{}
                }
            }
        }
        userViewModel.sendPostsErrorEvent(
            ErrorData(
                ok = true,
                errorCode = 1,
                error = "Error Getting Posts"
            )
        )
        collectJob.cancel()
    }

    @Test
    fun testPostListEvent() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            userViewModel.postsTasksEvent.collect {
                when (it) {
                    is PostDataEvent.Error -> {}
                    is PostDataEvent.Loading ->{}
                    is PostDataEvent.GetPosts ->{
                        assertTrue(it.dataReceived)
                    }
                    is PostDataEvent.RemoteErrorByNetwork ->{}
                }
            }
        }
        userViewModel.getPostsList(true)
        collectJob.cancel()
    }

    @Test
    fun testUserErrorEvent() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            userViewModel.usersTasksEvent.collect {
                when (it) {
                    is UserDataEvent.Error -> {
                        it.errorData.error?.let { errorStr -> assertTrue(errorStr.isNotEmpty()) }
                    }
                    is UserDataEvent.Loading ->{}
                    is UserDataEvent.GetUserList ->{}
                    is UserDataEvent.RemoteErrorByNetwork ->{}
                }
            }
        }
        userViewModel.sendErrorEvent(
            ErrorData(
                ok = true,
                errorCode = 1,
                error = "Error getting users list"
            )
        )
        collectJob.cancel()
    }

    @Test
    fun testUserListEvent() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            userViewModel.usersTasksEvent.collect {
                when (it) {
                    is UserDataEvent.Error -> {}
                    is UserDataEvent.Loading ->{}
                    is UserDataEvent.GetUserList ->{
                        assertTrue(it.dataReceived)
                    }
                    is UserDataEvent.RemoteErrorByNetwork ->{}
                }
            }
        }
        userViewModel.getUserListEvent(true)
        collectJob.cancel()
    }

    @Test
    fun testUserAndTheirPostsErrorEvent() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            userViewModel.userAndTheirPostTasksEvent.collect {
                when (it) {
                    is UserAndTheirPostEvent.Error -> {
                        it.exception.message?.let { message -> assertTrue(message.isNotEmpty()) }
                    }
                    is UserAndTheirPostEvent.Loading ->{}
                    is UserAndTheirPostEvent.UserWithTheirPosts ->{}
                }
            }
        }
        userViewModel.sendUserAndTheirPostsErrorEvent(java.lang.Exception("Error Reading Data"))
        collectJob.cancel()
    }

    @Test
    fun testUserAndTheirPostsErrorList() = kotlinx.coroutines.test.runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            userViewModel.userAndTheirPostTasksEvent.collect { event ->
                when (event) {
                    is UserAndTheirPostEvent.Error -> {}
                    is UserAndTheirPostEvent.Loading ->{}
                    is UserAndTheirPostEvent.UserWithTheirPosts ->
                        assertTrue(event.list.isNotEmpty())
                }
            }
        }
        userViewModel.getAllUsersAndTheirPostsList(initUserWithTheirPosts())
        collectJob.cancel()
    }

    private fun initUserWithTheirPosts() = object : ArrayList<UserAndTheirPostsData>() {
        init { // dummy list added to check the event...
            add(UserAndTheirPostsData())
            add(UserAndTheirPostsData())
            add(UserAndTheirPostsData())
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDownClass() {
        Mockito.framework().clearInlineMocks()
    }

}