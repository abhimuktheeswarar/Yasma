package msa.data

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.Validation
import com.jakewharton.retrofit2.converter.kotlinx.serialization.serializationConverterFactory
import io.reactivex.observers.TestObserver
import kotlinx.serialization.json.JSON
import msa.data.Utils.getJson
import msa.data.remote.RemoteDataStore
import msa.data.remote.YasmaApi
import msa.domain.entities.Post
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Created by Abhi Muktheeswarar.
 */

@RunWith(JUnit4::class)
class RemoteDataStoreTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteDataStore: RemoteDataStore

    @Before
    @Throws
    fun setUp() {

        mockWebServer = MockWebServer()
        mockWebServer.start()

        val httpUrl = mockWebServer.url("/")


        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BASIC
            )
        ).build()

        val yasmaApi = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(serializationConverterFactory(MediaType.get("application/json"), JSON))
            .baseUrl(httpUrl)
            .build()
            .create(YasmaApi::class.java)

        remoteDataStore = RemoteDataStore(yasmaApi)

    }

    @Test
    fun testGetPostDetail() {

        val remoteTestObserver = TestObserver<Result<Post, Exception>>()

        val path = "/posts/1"

        val mockReponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("/json/postdetail.json"))

        mockWebServer.enqueue(mockReponse)

        remoteDataStore.getPostDetail(1).subscribe(remoteTestObserver)
        remoteTestObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        remoteTestObserver.assertNoErrors()

        remoteTestObserver.assertValueCount(1)

        val request = mockWebServer.takeRequest()
        assertEquals(path, request.path)

        val remoteResult = remoteTestObserver.values().first()

        assertFalse(Validation(remoteResult).hasFailure)

    }

    @After
    @Throws
    fun tearDown() {
        mockWebServer.shutdown()
    }
}