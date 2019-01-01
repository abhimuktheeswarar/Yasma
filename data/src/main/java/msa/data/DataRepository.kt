package msa.data

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.success
import io.reactivex.Observable
import msa.domain.entities.*
import msa.domain.repository.Repository

/**
 * Created by Abhi Muktheeswarar.
 */

class DataRepository(private val dataStoreFactory: DataStoreFactory) : Repository {

    override fun getPosts(params: Params): Observable<Result<List<Post>, Exception>> {
        return if (params.loadFromCache && !dataStoreFactory.inMemoryDataStore.getPosts().isNullOrEmpty()) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getPosts())
        )
        else dataStoreFactory.remoteDataStore.getPosts().doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setPosts(
                    it
                )
            }
        }
    }

    override fun getPostDetail(params: Params): Observable<Result<Post, Exception>> {
        return if (params.loadFromCache && dataStoreFactory.inMemoryDataStore.getPostDetail(params.id) != null) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getPostDetail(params.id))
        )
        else dataStoreFactory.remoteDataStore.getPostDetail(params.id).doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setPostDetail(
                    it
                )
            }
        }
    }

    override fun getPostComments(params: Params): Observable<Result<List<PostComment>, Exception>> {
        return if (params.loadFromCache && dataStoreFactory.inMemoryDataStore.getPostComments(params.id) != null) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getPostComments(params.id))
        )
        else dataStoreFactory.remoteDataStore.getPostComments(params.id).doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setPostComments(
                    it
                )
            }
        }
    }

    override fun getAlbums(params: Params): Observable<Result<List<Album>, Exception>> {
        return if (params.loadFromCache && !dataStoreFactory.inMemoryDataStore.getAlbums().isNullOrEmpty()) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getAlbums())
        )
        else dataStoreFactory.remoteDataStore.getAlbums().doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setAlbums(
                    it
                )
            }
        }
    }

    override fun getAlbumDetail(params: Params): Observable<Result<Album, Exception>> {
        return if (params.loadFromCache && dataStoreFactory.inMemoryDataStore.getAlbumDetail(params.id) != null) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getAlbumDetail(params.id))
        )
        else dataStoreFactory.remoteDataStore.getAlbumDetail(params.id).doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setAlbumDetail(
                    it
                )
            }
        }
    }

    override fun getAlbumPhotos(params: Params): Observable<Result<List<Photo>, Exception>> {
        return if (params.loadFromCache && dataStoreFactory.inMemoryDataStore.getAlbumPhotos(params.id) != null) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getAlbumPhotos(params.id))
        )
        else dataStoreFactory.remoteDataStore.getAlbumPhotos(params.id).doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setAlbumDetail(
                    it
                )
            }
        }
    }

    override fun getUsers(params: Params): Observable<Result<List<User>, Exception>> {
        return if (params.loadFromCache && !dataStoreFactory.inMemoryDataStore.getUsers().isNullOrEmpty()) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getUsers())
        )
        else dataStoreFactory.remoteDataStore.getUsers().doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setUsers(
                    it
                )
            }
        }
    }

    override fun getUserDetail(params: Params): Observable<Result<User, Exception>> {
        return if (params.loadFromCache && dataStoreFactory.inMemoryDataStore.getUserDetail(params.id) != null) Observable.just(
            Result.of(dataStoreFactory.inMemoryDataStore.getUserDetail(params.id))
        )
        else dataStoreFactory.remoteDataStore.getUserDetail(params.id).doOnNext { result ->
            result.success {
                dataStoreFactory.inMemoryDataStore.setUserDetail(
                    it
                )
            }
        }
    }
}