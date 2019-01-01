package msa.yasma

import android.app.Application
import msa.yasma.di.appModule
import msa.yasma.di.stateMachineModule
import msa.yasma.di.useCaseModule
import msa.yasma.di.viewModelModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

/**
 * Created by Abhi Muktheeswarar.
 */

class YasmaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin(this, listOf(appModule, stateMachineModule, useCaseModule, viewModelModule))
    }
}