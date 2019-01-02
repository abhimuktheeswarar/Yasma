package msa.data.inmemory

import androidx.collection.SparseArrayCompat
import msa.domain.entities.*

/**
 * Created by Abhi Muktheeswarar.
 */

class InMemoryDataStore {

    private var posts = LinkedHashMap<Int, Post>()
    private val postDetails = SparseArrayCompat<Post>()
    private val postComments = SparseArrayCompat<List<PostComment>>()
    private var albums = LinkedHashMap<Int, Album>()
    private var albumDetails = SparseArrayCompat<Album>()
    private val albumPhotos = SparseArrayCompat<List<Photo>>()
    private var users = LinkedHashMap<Int, User>()
    private val userDetails = SparseArrayCompat<User>()

    fun setPosts(postList: List<Post>) {

        posts = LinkedHashMap(postList.associateBy { it.id })

    }

    fun setPostDetail(postDetail: Post) {


        postDetails.put(postDetail.id, postDetail)

    }

    fun setPostComments(postCommentList: List<PostComment>) {

        postComments.put(postCommentList.first().id, postCommentList)

    }

    fun setAlbums(albumList: List<Album>) {

        albums = LinkedHashMap(albumList.associateBy { it.id })

    }

    fun setAlbumDetail(albumDetail: Album) {

        albumDetails.put(albumDetail.id, albumDetail)
    }

    fun setAlbumDetail(photos: List<Photo>) {

        albumPhotos.put(photos.first().albumId, photos)

    }

    fun setPhotos(photoList: List<Photo>) {

        albumPhotos.put(photoList.first().id, photoList)

    }

    fun setUsers(userList: List<User>) {

        users = LinkedHashMap(userList.associateBy { it.id })

    }

    fun setUserDetail(userDetail: User) {

        userDetails.put(userDetail.id, userDetail)

    }

    fun getPosts(): List<Post>? {

        return if (posts.isNotEmpty()) posts.values.toList() else null
    }

    fun getPostDetail(postId: Int): Post? {

        return postDetails[postId]
    }

    fun getPostComments(postId: Int): List<PostComment>? {

        return postComments[postId]
    }

    fun getAlbums(): List<Album>? {

        return if (albums.isNotEmpty()) albums.values.toList() else null
    }

    fun getAlbumDetail(albumId: Int): Album? {

        return albumDetails[albumId]
    }

    fun getAlbumPhotos(albumId: Int): List<Photo>? {

        return albumPhotos[albumId]
    }

    fun getUsers(): List<User>? {

        return if (users.isNotEmpty()) users.values.toList() else null
    }

    fun getUserDetail(userId: Int): User? {

        return users[userId]
    }

}