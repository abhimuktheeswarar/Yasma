package msa.yasma.di

import msa.data.DataRepository
import msa.data.DataStoreFactory
import msa.domain.repository.Repository
import org.koin.dsl.module.module

/**
 * Created by Abhi Muktheeswarar.
 */

val appModule = module {

    single { DataStoreFactory(get()) }
    single<Repository> { DataRepository(get()) }

}

val stateMachineModule = module {


}

val useCaseModule = module {


}

val viewModelModule = module {


}