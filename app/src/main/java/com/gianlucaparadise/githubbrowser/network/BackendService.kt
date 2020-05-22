package com.gianlucaparadise.githubbrowser.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.gianlucaparadise.githubbrowser.AuthenticatedUserQuery
import com.gianlucaparadise.githubbrowser.BuildConfig
import com.gianlucaparadise.githubbrowser.data.User
import okhttp3.*
import java.lang.Exception

private const val GITHUB_ENDPOINT: String = "https://api.github.com/graphql"

object BackendService {

    private val client: ApolloClient

    init {
        val authHeader = "Bearer ${BuildConfig.PERSONAL_ACCESS_TOKEN}"
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    return response.request.newBuilder().header("Authorization", authHeader)
                        .build()
                }
            })
            .build()

        client = ApolloClient.builder()
            .okHttpClient(okHttpClient)
            .serverUrl(GITHUB_ENDPOINT)
            .build()
    }

    suspend fun retrieveAuthenticatedUser(): User? {
        try {
            val user = client
                .query(AuthenticatedUserQuery())
                .toDeferred()
                .await()

            val userFragment = user.data?.viewer?.fragments?.userFragment
            return User.fromUserFragment(userFragment)

        } catch (apolloEx: ApolloException) {
            throw Exception("Error while retrieving Authenticated User")
        }
    }
}