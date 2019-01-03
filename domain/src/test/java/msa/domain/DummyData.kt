package msa.domain

import msa.domain.entities.Post
import msa.domain.entities.User

/**
 * Created by Abhi Muktheeswarar.
 */

object DummyData {


    fun getPosts(): List<Post> {

        val posts = arrayListOf<Post>()

        for (i in 1..10) {

            val post = Post(id = i, title = "t$i", userId = i, body = "body$i")

            posts.add(post)
        }

        return posts
    }

    fun getUsers(): List<User> {

        val users = arrayListOf<User>()

        for (i in 1..10) {

            val company = User.Company(bs = "bs$i", catchPhrase = "catchPhrase$i", name = "name$i")
            val geo = User.Address.Geo("lat$i", "lng$i")
            val address =
                User.Address(city = "city$i", geo = geo, street = "street$i", suite = "suite$i", zipcode = "zipcode$i")
            val user = User(
                id = i,
                address = address,
                company = company,
                email = "$i@email.com",
                name = "name$i",
                phone = "phone$i",
                username = "username$i",
                website = "website$i"
            )

            users.add(user)
        }

        return users
    }

    fun getPostsWithUser(): List<Pair<Post, User>> {

        val posts = getPosts()
        val users = getUsers().associateBy { it.id }

        val postsWithUser = arrayListOf<Pair<Post, User>>()

        for (post in posts) {

            postsWithUser.add(Pair(post, users[post.userId]!!))
        }

        return postsWithUser
    }

}