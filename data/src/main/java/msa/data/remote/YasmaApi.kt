package msa.data.remote

import io.reactivex.Observable
import msa.domain.entities.*
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Abhi Muktheeswarar.
 */

interface YasmaApi {

    @GET("posts")
    fun getPosts(): Observable<List<Post>>

    @GET("posts/{postId}")
    fun getPostDetail(@Path("postId") postId: Int): Observable<Post>

    @GET("posts/{postId}/comments")
    fun getPostComments(@Path("postId") postId: Int): Observable<List<PostComment>>


    @GET("albums")
    fun getAlbums(): Observable<List<Album>>

    @GET("albums/{albumId}")
    fun getAlbumDetail(@Path("albumId") albumId: Int): Observable<Album>

    @GET("albums/{albumId}/photos")
    fun getAlbumPhotos(@Path("albumId") albumId: Int): Observable<List<Photo>>


    @GET("users")
    fun getUsers(): Observable<List<User>>

    @GET("users/{userId}")
    fun getUserDetail(@Path("userId") userId: Int): Observable<User>
}