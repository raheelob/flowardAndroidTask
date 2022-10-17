package com.floward.androidtask.view.network

import com.floward.androidtask.data.api.APIsCollection
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class FetchUsersAndPostTest {
    private lateinit var service: APIsCollection
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIsCollection::class.java)
    }

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    @Test
    fun getUserListRequest() {
        runBlocking {
            enqueueMockResponse("UsersResponse.json")
            val responseBody = service.getUsersList()
            assertTrue(responseBody.toString().isNotEmpty())
            val request = server.takeRequest()
            assertTrue(request.path.equals("/users"))
        }
    }

    @Test
    fun getPostRequest() {
        runBlocking {
            enqueueMockResponse("PostsResponse.json")
            val responseBody = service.getUserPost()
            assertTrue(responseBody.toString().isNotEmpty())
            val request = server.takeRequest()
            assertTrue(request.path.equals("/posts"))
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}