package com.example.indok.Responses;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    ////////////////////////////getting all posts/////////////////////
    @GET("posts.php")
    Call<Users> performAllPosts();
}
