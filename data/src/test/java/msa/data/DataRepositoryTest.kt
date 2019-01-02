package msa.data

import android.content.Context
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.Validation
import io.reactivex.observers.TestObserver
import msa.data.Utils.getJson
import msa.domain.entities.Params
import msa.domain.entities.Post
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Created by Abhi Muktheeswarar.
 */

@RunWith(JUnit4::class)
class DataRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var dataStoreFactory: DataStoreFactory
    private lateinit var dataRepository: DataRepository

    @Before
    @Throws
    fun setUp() {

        mockWebServer = MockWebServer()

        mockWebServer.start()

        val httpUrl = mockWebServer.url("/")

        dataStoreFactory = DataStoreFactory(mock(Context::class.java), httpUrl.toString())
        dataRepository = DataRepository(dataStoreFactory)

    }

    @Test
    fun testGetPosts() {

        val remoteTestObserver = TestObserver<Result<List<Post>, Exception>>()

        val path = "/posts"

        val mockReponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("/json/posts.json"))

        mockWebServer.enqueue(mockReponse)

        val params = Params(loadFromCache = false, id = -1)

        dataRepository.getPosts(params).subscribe(remoteTestObserver)
        remoteTestObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        remoteTestObserver.assertNoErrors()

        remoteTestObserver.assertValueCount(1)

        val request = mockWebServer.takeRequest()
        assertEquals(path, request.path)

        val remoteResult = remoteTestObserver.values().first()

        assertFalse(Validation(remoteResult).hasFailure)

        val inMemoryTestObserver = TestObserver<Result<List<Post>, Exception>>()

        dataRepository.getPosts(params.copy(loadFromCache = true)).subscribe(inMemoryTestObserver)

        val inMemoryResult = remoteTestObserver.values().first()

        assertNotNull(inMemoryResult.component1())

        assert(inMemoryResult.component1()?.isNotEmpty() == true)

    }

    @Test
    fun testGetPostDetail() {

        val remoteTestObserver = TestObserver<Result<Post, Exception>>()

        val path = "/posts/1"

        val mockReponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("/json/postdetail.json"))

        mockWebServer.enqueue(mockReponse)

        val params = Params(loadFromCache = false, id = 1)

        dataRepository.getPostDetail(params).subscribe(remoteTestObserver)
        remoteTestObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        remoteTestObserver.assertNoErrors()

        remoteTestObserver.assertValueCount(1)

        val request = mockWebServer.takeRequest()
        assertEquals(path, request.path)

        val remoteResult = remoteTestObserver.values().first()

        assertFalse(Validation(remoteResult).hasFailure)

        val inMemoryTestObserver = TestObserver<Result<Post, Exception>>()

        dataRepository.getPostDetail(params.copy(loadFromCache = true)).subscribe(inMemoryTestObserver)

        val inMemoryResult = remoteTestObserver.values().first()

        assertNotNull(inMemoryResult.component1())

        assert(inMemoryResult.component1()?.id == 1)

    }

    @After
    @Throws
    fun tearDown() {
        mockWebServer.shutdown()
    }
}