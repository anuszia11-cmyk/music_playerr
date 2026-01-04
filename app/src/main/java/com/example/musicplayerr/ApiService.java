package com.example.musicplayerr;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface for Retrofit to define the API endpoints.
 * This acts as the bridge between the Android app and the remote server.
 */
public interface ApiService {

    // The @GET annotation specifies the endpoint to be appended to the Base URL
    // In this case, it targets: https://jsonplaceholder.typicode.com/posts
    @GET("posts")
    Call<List<post>> getPosts();

    /* Note: 'post' (lowercase 'p') must match your model class filename
       exactly for the GSON converter to map the JSON data correctly.
    */
}