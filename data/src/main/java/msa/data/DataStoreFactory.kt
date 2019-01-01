package msa.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.serializationConverterFactory
import kotlinx.serialization.json.JSON
import msa.data.inmemory.InMemoryDataStore
import msa.data.local.LocalDataStore
import msa.data.remote.RemoteDataStore
import msa.data.remote.YasmaApi
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Created by Abhi Muktheeswarar.
 */

class DataStoreFactory(context: Context) {

    val inMemoryDataStore: InMemoryDataStore = InMemoryDataStore()
    val localDataStore: LocalDataStore = LocalDataStore()
    val remoteDataStore: RemoteDataStore

    init {

        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BASIC
            )
        ).build()

        val yasmaApi = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(serializationConverterFactory(MediaType.get("application/json"), JSON))
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()
            .create(YasmaApi::class.java)

        remoteDataStore = RemoteDataStore(yasmaApi)
    }
}