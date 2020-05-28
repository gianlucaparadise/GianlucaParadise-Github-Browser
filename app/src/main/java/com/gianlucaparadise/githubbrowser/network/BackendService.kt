package com.gianlucaparadise.githubbrowser.network

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.gianlucaparadise.githubbrowser.*
import com.gianlucaparadise.githubbrowser.data.*
import com.gianlucaparadise.githubbrowser.type.AddStarInput
import com.gianlucaparadise.githubbrowser.type.RemoveStarInput
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

        //region NormalizedCacheFactory
        // Offline cache is handled automatically by Apollo client
        val cacheFactory =
            SqlNormalizedCacheFactory(MainApplication.applicationContext, "github_cache")
        val resolver: CacheKeyResolver = object : CacheKeyResolver() {
            override fun fromFieldRecordSet(
                field: ResponseField,
                recordSet: Map<String, Any>
            ): CacheKey {
                return formatCacheKey(recordSet["id"] as String?)
            }

            override fun fromFieldArguments(
                field: ResponseField,
                variables: Operation.Variables
            ): CacheKey {
                return formatCacheKey(field.resolveArgument("id", variables) as String?)
            }

            private fun formatCacheKey(id: String?) = when {
                id.isNullOrEmpty() -> CacheKey.NO_KEY
                else -> CacheKey.from(id)
            }
        }
        //endregion

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
            .normalizedCache(cacheFactory, resolver)
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

    /**
     * Add or remove a star to the input repository
     * @return new Stargazers Count
     */
    suspend fun toggleStar(repository: Repository): Starrable? {
        return if (repository.viewerHasStarred) {
            removeStar(repository)
        } else {
            addStar(repository)
        }
    }

    /**
     * Add a star to the input repository
     * @return new Stargazers Count
     */
    private suspend fun addStar(repository: Repository): Starrable? {
        try {
            val input = AddStarInput(repository.id)
            val response = graphQlClient
                .mutate(AddStarMutation(input))
                .toDeferred()
                .await()

            Log.d("Backend", "AddStar response: $response")

            val starrableFragment = response.data?.addStar?.starrable?.fragments?.starrableFragment
                ?: throw Exception("Empty Response")

            return Starrable.fromStarrableFragment(starrableFragment)

        } catch (apolloEx: ApolloException) {
            throw Exception("Error while adding star to Repository")
        }
    }

    /**
     * Remove a star from the input repository
     * @return new Stargazers Count
     */
    private suspend fun removeStar(repository: Repository): Starrable? {
        try {
            val input = RemoveStarInput(repository.id)
            val response = graphQlClient
                .mutate(RemoveStarMutation(input))
                .toDeferred()
                .await()

            Log.d("Backend", "RemoveStar response: $response")

            val starrableFragment =
                response.data?.removeStar?.starrable?.fragments?.starrableFragment
                    ?: throw Exception("Empty Response")

            return Starrable.fromStarrableFragment(starrableFragment)

        } catch (apolloEx: ApolloException) {
            throw Exception("Error while removing star from Repository")
        }
    }

    suspend fun retrieveAccessToken(code: String, state: String): AccessTokenModel {
        val clientId = BuildConfig.CLIENT_ID
        val clientSecret = BuildConfig.CLIENT_SECRET
        return this.restClient.postAccessToken(clientId, clientSecret, code, state)
    }
}