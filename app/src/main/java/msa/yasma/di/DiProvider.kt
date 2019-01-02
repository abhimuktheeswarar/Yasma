package msa.yasma.di

import io.reactivex.android.schedulers.AndroidSchedulers
import msa.data.DataRepository
import msa.data.DataStoreFactory
import msa.domain.repository.Repository
import msa.domain.statemachine.PostListStateMachine
import msa.domain.usecases.GetPosts
import msa.yasma.post.list.PostListViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by Abhi Muktheeswarar.
 */

val appModule = module {

    single { DataStoreFactory(get()) }
    single<Repository> { DataRepository(get()) }
    single { AndroidSchedulers.mainThread() }
}

val stateMachineModule = module {

    factory { PostListStateMachine(get()) }

}

val useCaseModule = module {

    factory { GetPosts(get(), get()) }
}

val viewModelModule = module {

    viewModel { PostListViewModel(get()) }

}