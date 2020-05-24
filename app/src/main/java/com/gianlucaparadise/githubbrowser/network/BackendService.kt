package com.gianlucaparadise.githubbrowser.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.gianlucaparadise.githubbrowser.*
import com.gianlucaparadise.githubbrowser.data.PaginatedResponse
import com.gianlucaparadise.githubbrowser.data.Repository
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
                ?: throw Exception("Empty Response")

            return User.fromUserFragment(userFragment)

        } catch (apolloEx: ApolloException) {
            throw Exception("Error while retrieving Authenticated User")
        }
    }

    suspend fun retrieveAuthenticatedUserRepositories(
        first: Int,
        startCursor: String? = null
    ): PaginatedResponse<Repository> {
        try {
            val repositories = client
                .query(
                    AuthenticatedUserRepositoriesQuery(
                        Input.fromNullable(first),
                        Input.optional(startCursor)
                    )
                )
                .toDeferred()
                .await()

            val repositoriesResponse = repositories.data?.viewer?.repositories
                ?: throw Exception("Empty Response")

            return Repository.fromRepositoriesResponse(repositoriesResponse)

        } catch (apolloEx: ApolloException) {
            throw Exception("Error while retrieving Authenticated User's Repositories")
        }
    }

    suspend fun searchUsers(
        query: String,
        first: Int,
        startCursor: String? = null
    ): PaginatedResponse<User> {
        try {
            val users = client
                .query(
                    SearchUsersQuery(
                        query,
                        Input.fromNullable(first),
                        Input.optional(startCursor)
                    )
                )
                .toDeferred()
                .await()

            val usersResponse = users.data?.search
                ?: throw Exception("Empty Response")

            return User.fromSearchUsersResponse(usersResponse)

        } catch (apolloEx: ApolloException) {
            throw Exception("Error while searching for Users")
        }
    }

    suspend fun searchRepositories(
        query: String,
        first: Int,
        startCursor: String? = null
    ): PaginatedResponse<Repository> {
        try {
            val repositories = client
                .query(
                    SearchRepositoriesQuery(
                        query,
                        Input.fromNullable(first),
                        Input.optional(startCursor)
                    )
                )
                .toDeferred()
                .await()

            val repositoriesResponse = repositories.data?.search
                ?: throw Exception("Empty Response")

            return Repository.fromSearchRepositoriesResponse(repositoriesResponse)

        } catch (apolloEx: ApolloException) {
            throw Exception("Error while searching for Repositories")
        }
    }
}