package msa.yasma.di

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import msa.data.DataRepository
import msa.data.DataStoreFactory
import msa.domain.repository.Repository
import msa.domain.statemachine.AlbumDetailStateMachine
import msa.domain.statemachine.AlbumListStateMachine
import msa.domain.statemachine.PostDetailStateMachine
import msa.domain.statemachine.PostListStateMachine
import msa.domain.usecases.GetAlbumDetail
import msa.domain.usecases.GetAlbums
import msa.domain.usecases.GetPostDetail
import msa.domain.usecases.GetPosts
import msa.yasma.album.detail.AlbumDetailViewModel
import msa.yasma.album.list.AlbumListViewModel
import msa.yasma.post.detail.PostDetailViewModel
import msa.yasma.post.list.PostListViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by Abhi Muktheeswarar.
 */

val appModule = module {

    single { DataStoreFactory(get()) }
    single<Repository> { DataRepository(get()) }
    single(name = "threadExecutor") { Schedulers.io() }
    single(name = "postExecutionScheduler") { AndroidSchedulers.mainThread() }
}

val stateMachineModule = module {

    factory { PostListStateMachine(get()) }
    factory { PostDetailStateMachine(get()) }

    factory { AlbumListStateMachine(get()) }
    factory { AlbumDetailStateMachine(get()) }

}

val useCaseModule = module {

    factory { GetPosts(get(), get(name = "threadExecutor"), get(name = "postExecutionScheduler")) }
    factory { GetPostDetail(get(), get(name = "threadExecutor"), get(name = "postExecutionScheduler")) }

    factory { GetAlbums(get(), get(name = "threadExecutor"), get(name = "postExecutionScheduler")) }
    factory { GetAlbumDetail(get(), get(name = "threadExecutor"), get(name = "postExecutionScheduler")) }
}

val viewModelModule = module {

    viewModel { PostListViewModel(get()) }
    viewModel { PostDetailViewModel(get()) }

    viewModel { AlbumListViewModel(get()) }
    viewModel { AlbumDetailViewModel(get()) }

}