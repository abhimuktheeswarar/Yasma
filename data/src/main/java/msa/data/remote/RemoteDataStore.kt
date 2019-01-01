package msa.data.remote

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import msa.domain.entities.*

/**
 * Created by Abhi Muktheeswarar.
 */

class RemoteDataStore(private val yasmaApi: YasmaApi) {

    fun getPosts(): Observable<Result<List<Post>, Exception>> {
        return yasmaApi.getPosts().map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }

    fun getPostDetail(postId: Int): Observable<Result<Post, Exception>> {
        return yasmaApi.getPostDetail(postId).map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }

    fun getPostComments(postId: Int): Observable<Result<List<PostComment>, Exception>> {
        return yasmaApi.getPostComments(postId).map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }

    fun getAlbums(): Observable<Result<List<Album>, Exception>> {
        return yasmaApi.getAlbums().map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }

    fun getAlbumDetail(albumId: Int): Observable<Result<Album, Exception>> {
        return yasmaApi.getAlbumDetail(albumId).map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }

    fun getAlbumPhotos(albumId: Int): Observable<Result<List<Photo>, Exception>> {
        return yasmaApi.getAlbumPhotos(albumId).map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }

    fun getUsers(): Observable<Result<List<User>, Exception>> {
        return yasmaApi.getUsers().map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }

    fun getUserDetail(userId: Int): Observable<Result<User, Exception>> {
        return yasmaApi.getUserDetail(userId).map { Result.of(it) }.onErrorReturn { Result.error(Exception(it)) }
    }
}


