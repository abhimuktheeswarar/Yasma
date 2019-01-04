package msa.yasma

import android.app.Application
import android.os.AsyncTask
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import msa.data.DataRepository
import msa.data.DataStoreFactory
import msa.domain.repository.Repository
import msa.yasma.di.stateMachineModule
import msa.yasma.di.useCaseModule
import msa.yasma.di.viewModelModule
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import timber.log.Timber


/**
 * Created by Abhi Muktheeswarar.
 */

class TestYasmaApplication : Application() {

    private lateinit var mockWebServer: MockWebServer
    lateinit var httpUrl: HttpUrl

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        mockWebServer = MockWebServer()

        AsyncTask.execute {
            mockWebServer.start()
            httpUrl = mockWebServer.url("/")
        }

        startKoin(this, listOf(testAppModule, stateMachineModule, useCaseModule, viewModelModule))
    }

    override fun onTerminate() {
        super.onTerminate()
        mockWebServer.close()
    }

    fun getMockWebServer() = mockWebServer
}

val testAppModule = module {

    single { DataStoreFactory(get(), (androidApplication() as TestYasmaApplication).httpUrl.toString()) }
    single<Repository> { DataRepository(get()) }
    single(name = "threadExecutor") { Schedulers.io() }
    single(name = "postExecutionScheduler") { AndroidSchedulers.mainThread() }
}