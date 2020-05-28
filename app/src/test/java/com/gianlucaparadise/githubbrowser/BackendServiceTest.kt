package com.gianlucaparadise.githubbrowser

import com.gianlucaparadise.githubbrowser.data.PaginatedResponse
import com.gianlucaparadise.githubbrowser.data.Repository
import com.gianlucaparadise.githubbrowser.network.BackendService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * These tests work only if PersonalAccessToken is used in place of regular access token
 * TODO: Mock SharedPreferencesManager class and use PersonalAccessToken
 */
class BackendServiceTest {
    @Test
    fun authenticatedUserTest() {
        val user = runBlocking {
            BackendService.retrieveAuthenticatedUser()
        }

        assertNotNull("User shouldn't be null", user)
        assertFalse("Login Name should never be empty", user?.login.isNullOrEmpty())
    }

    @Test
    fun authenticatedUserRepositoriesTest() {
        val repositories = runBlocking {
            BackendService.retrieveAuthenticatedUserRepositories(10)
        }

        assertNotNull("Repository list shouldn't be null", repositories)
    }

    @Test
    fun authenticatedUserRepositoriesPaginationTest() {
        // This test will check if pagination is working correctly with BackendService

        // Getting a page of two elements
        val firstTwoRepositories = runBlocking {
            BackendService.retrieveAuthenticatedUserRepositories(2)
        }

        assertNotNull("Repository list shouldn't be null", firstTwoRepositories)
        assertEquals("There are less than 2 repositories, pagination can't be tested", 2, firstTwoRepositories?.nodes?.size)

        // Getting two pages of one elements
        val firstRepository = runBlocking {
            BackendService.retrieveAuthenticatedUserRepositories(1)
        }

        assertNotNull("Repository list shouldn't be null", firstRepository?.endCursor)
        assertTrue("There should be at least one other page", firstRepository?.hasNextPage!!)

        val secondRepository = runBlocking {
            BackendService.retrieveAuthenticatedUserRepositories(1, firstRepository.endCursor)
        }

        assertNotNull("Repository list shouldn't be null", secondRepository)

        fun getNameByIndex(repo: PaginatedResponse<Repository>?, index: Int): String? {
            return repo?.nodes?.get(index)?.name
        }

        assertEquals("Names for index 0 are not matching", getNameByIndex(firstTwoRepositories, 0),  getNameByIndex(firstRepository, 0))
        assertEquals("Names for index 1 are not matching", getNameByIndex(firstTwoRepositories, 1),  getNameByIndex(secondRepository, 0))
    }

    @Test
    fun searchUsers() {
        val users = runBlocking {
            BackendService.searchUsers("jake", 1)
        }

        assertNotNull("Users list shouldn't be null", users)
    }

    @Test
    fun searchRepositories() {
        val repositories = runBlocking {
            BackendService.searchRepositories("android", 1)
        }

        assertNotNull("Repositories list shouldn't be null", repositories)
    }
}