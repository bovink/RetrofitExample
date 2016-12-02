package com.example.bovink.retrofitexample.api;

import com.example.bovink.retrofitexample.model.Repo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * com.example.bovink.retrofitexample.api
 *
 * @author bovink
 * @since 2016/12/1
 */

public interface API {
    @GET("/users/{user}/repos")
    Call<List<Repo>> getRepoList(@Path("user") String username, @Query("page") String page, @Query("per_page") String per_page);

    @GET("/users/{user}/repos")
    Call<List<Repo>> getRepoList(@Path("user") String username, @QueryMap Map<String, String> options);
}
