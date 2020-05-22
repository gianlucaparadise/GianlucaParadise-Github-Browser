package com.gianlucaparadise.githubbrowser

import com.gianlucaparadise.githubbrowser.network.BackendService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class BackendServiceTest {
    @Test
    fun authenticatedUserTest() {
        val user = runBlocking {
            BackendService.retrieveAuthenticatedUser()
        }

        assertNotNull("User shouldn't be null", user)
        assertFalse("Login Name should never be empty", user?.login.isNullOrEmpty())
    }
}