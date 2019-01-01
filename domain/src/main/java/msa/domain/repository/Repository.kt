package msa.domain.repository

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import msa.domain.entities.*

/**
 * Created by Abhi Muktheeswarar.
 */

interface Repository {

    fun getPosts(params: Params): Observable<Result<List<Post>, Exception>>

    fun getPostDetail(params: Params): Observable<Result<Post, Exception>>

    fun getPostComments(params: Params): Observable<Result<List<PostComment>, Exception>>

    fun getAlbums(params: Params): Observable<Result<List<Album>, Exception>>

    fun getAlbumDetail(params: Params): Observable<Result<Album, Exception>>

    fun getAlbumPhotos(params: Params): Observable<Result<List<Photo>, Exception>>

    fun getUsers(params: Params): Observable<Result<List<User>, Exception>>

    fun getUserDetail(params: Params): Observable<Result<User, Exception>>
}