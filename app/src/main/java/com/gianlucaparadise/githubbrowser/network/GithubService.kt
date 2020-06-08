package com.gianlucaparadise.githubbrowser.network

import com.gianlucaparadise.githubbrowser.vo.AccessTokenModel
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface GithubService {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    suspend fun postAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Query("state") state: String
        //, @Query("redirect_uri") redirectUri: String
    ): AccessTokenModel
}