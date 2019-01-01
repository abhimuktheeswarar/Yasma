package msa.data.inmemory

import msa.domain.entities.*

/**
 * Created by Abhi Muktheeswarar.
 */

class InMemoryDataStore {

    private var posts = LinkedHashMap<Int, Post>()
    private val postDetails = LinkedHashMap<Int, Post>()
    private val postComments = LinkedHashMap<Int, List<PostComment>>()
    private var albums = LinkedHashMap<Int, Album>()
    private var albumDetails = LinkedHashMap<Int, Album>()
    private val albumPhotos = LinkedHashMap<Int, List<Photo>>()
    private var users = LinkedHashMap<Int, User>()
    private val userDetails = LinkedHashMap<Int, User>()

    fun setPosts(postList: List<Post>) {

        posts = LinkedHashMap(postList.associateBy { it.id })

    }

    fun setPostDetail(postDetail: Post) {

        postDetails[postDetail.id] = postDetail

    }

    fun setPostComments(postCommentList: List<PostComment>) {

        postComments[postCommentList.first().id] = postCommentList

    }

    fun setAlbums(albumList: List<Album>) {

        albums = LinkedHashMap(albumList.associateBy { it.id })

    }

    fun setAlbumDetail(albumDetail: Album) {

        albumDetails[albumDetail.id] = albumDetail

    }

    fun setAlbumDetail(photos: List<Photo>) {

        albumPhotos[photos.first().albumId] = photos

    }

    fun setPhotos(photoList: List<Photo>) {

        albumPhotos[photoList.first().id] = photoList

    }

    fun setUsers(userList: List<User>) {

        users = LinkedHashMap(userList.associateBy { it.id })

    }

    fun setUserDetail(userDetail: User) {

        userDetails[userDetail.id] = userDetail

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