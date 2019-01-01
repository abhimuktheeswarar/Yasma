package msa.data.local

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import msa.domain.entities.*

/**
 * Created by Abhi Muktheeswarar.
 */

class LocalDataStore {

    fun getPosts(): Observable<Result<List<Post>, Exception>> {
        TODO("not implemented")
    }

    fun getPostDetail(postId: Int): Observable<Result<Post, Exception>> {
        TODO("not implemented")
    }

    fun getPostComments(postId: Int): Observable<Result<List<PostComment>, Exception>> {
        TODO("not implemented")
    }

    fun getAlbums(): Observable<Result<List<Album>, Exception>> {
        TODO("not implemented")
    }

    fun getAlbumDetail(albumId: Int): Observable<Result<Album, Exception>> {
        TODO("not implemented")
    }

    fun getAlbumPhotos(albumId: Int): Observable<Result<List<Photo>, Exception>> {
        TODO("not implemented")
    }

    fun getUsers(): Observable<Result<List<User>, Exception>> {
        TODO("not implemented")
    }

    fun getUserDetail(userId: Int): Observable<Result<User, Exception>> {
        TODO("not implemented")
    }
}