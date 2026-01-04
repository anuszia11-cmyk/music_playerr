package com.example.musicplayerr;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("posts")
    Call<List<post>> getPosts(); // This Post must match the class name exactly
}