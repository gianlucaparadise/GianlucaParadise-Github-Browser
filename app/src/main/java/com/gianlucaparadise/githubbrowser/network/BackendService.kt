package com.gianlucaparadise.githubbrowser.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.gianlucaparadise.githubbrowser.*
import com.gianlucaparadise.githubbrowser.data.*
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

private const val GITHUB_API_ENDPOINT = "https://api.github.com/graphql"
private const val GITHUB_WEBSITE_ENDPOINT = "https://github.com"

object BackendService {

    /**
     * This is the client used to interact with Github's GraphQL Backend
     */
    private val graphQlClient: ApolloClient

    private val restClient: GithubService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    val authHeader = "Bearer ${SharedPreferencesManager.accessToken}"
                    return response.request.newBuilder().header("Authorization", authHeader)
                        .build()
                }
            })
            .build()

        graphQlClient = ApolloClient.builder()
            .okHttpClient(okHttpClient)
            .serverUrl(GITHUB_API_ENDPOINT)
            .build()

        val retrofitClient = Retrofit.Builder()
            .baseUrl(GITHUB_WEBSITE_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        restClient = retrofitClient.create(GithubService::class.java)
    }

    suspend fun retrieveAuthenticatedUser(): User? {
        try {
            val user = graphQlClient
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
            val repositories = graphQlClient
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
            val users = graphQlClient
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
            val repositories = graphQlClient
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

    suspend fun retrieveAccessToken(code: String, state: String): AccessTokenModel {
        val clientId = BuildConfig.CLIENT_ID
        val clientSecret = BuildConfig.CLIENT_SECRET
        return this.restClient.postAccessToken(clientId, clientSecret, code, state)
    }
}