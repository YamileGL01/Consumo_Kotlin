package com.example.pruebas.Network

import com.example.pruebas.Models.NewPost
import com.example.pruebas.Models.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiConsume {
    @GET("/posts")
    fun getPosts(): Call<List<Post>>

    @POST("/posts")
    fun newPost(@Body post: NewPost): Call<Post>

    @PUT("posts/{postId}")
    fun updatePost(
        @Path("postId") postId: String,
        @Body post: Post
    ): Call<Post>

    @DELETE("posts/{postId}")
    fun deletePost(
        @Path("postId") postId: String,
    ): Call<Void>
}