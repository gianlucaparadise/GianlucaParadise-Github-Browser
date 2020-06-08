package com.gianlucaparadise.githubbrowser

import com.gianlucaparadise.githubbrowser.vo.PaginatedResponse
import com.gianlucaparadise.githubbrowser.vo.Repo
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
    fun authenticatedUserReposTest() {
        val repos = runBlocking {
            BackendService.retrieveAuthenticatedUserRepos(10)
        }

        assertNotNull("Repo list shouldn't be null", repos)
    }

    @Test
    fun authenticatedUserReposPaginationTest() {
        // This test will check if pagination is working correctly with BackendService

        // Getting a page of two elements
        val firstTwoRepos = runBlocking {
            BackendService.retrieveAuthenticatedUserRepos(2)
        }

        assertNotNull("Repo list shouldn't be null", firstTwoRepos)
        assertEquals("There are less than 2 repos, pagination can't be tested", 2, firstTwoRepos.nodes.size)

        // Getting two pages of one elements
        val firstRepo = runBlocking {
            BackendService.retrieveAuthenticatedUserRepos(1)
        }

        assertNotNull("Repo list shouldn't be null", firstRepo.endCursor)
        assertTrue("There should be at least one other page", firstRepo.hasNextPage)

        val secondRepo = runBlocking {
            BackendService.retrieveAuthenticatedUserRepos(1, firstRepo.endCursor)
        }

        assertNotNull("Repo list shouldn't be null", secondRepo)

        fun getNameByIndex(repo: PaginatedResponse<Repo>?, index: Int): String? {
            return repo?.nodes?.get(index)?.name
        }

        assertEquals("Names for index 0 are not matching", getNameByIndex(firstTwoRepos, 0),  getNameByIndex(firstRepo, 0))
        assertEquals("Names for index 1 are not matching", getNameByIndex(firstTwoRepos, 1),  getNameByIndex(secondRepo, 0))
    }

    @Test
    fun searchUsers() {
        val users = runBlocking {
            BackendService.searchUsers("jake", 1)
        }

        assertNotNull("Users list shouldn't be null", users)
    }

    @Test
    fun searchRepos() {
        val repos = runBlocking {
            BackendService.searchRepos("android", 1)
        }

        assertNotNull("Repos list shouldn't be null", repos)
    }
}