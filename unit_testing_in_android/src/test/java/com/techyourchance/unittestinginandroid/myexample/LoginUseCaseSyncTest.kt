package com.techyourchance.unittestinginandroid.myexample

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.techyourchance.unittestinginandroid.myexample.authtoken.AuthTokenCache
import com.techyourchance.unittestinginandroid.myexample.eventbus.EventBusPoster
import com.techyourchance.unittestinginandroid.myexample.eventbus.LoggedInEvent
import com.techyourchance.unittestinginandroid.myexample.networking.LoginHttpEndpointSync
import com.techyourchance.unittestinginandroid.myexample.networking.LoginHttpEndpointSync.EndpointResult
import com.techyourchance.unittestinginandroid.myexample.networking.LoginHttpEndpointSync.EndpointResultStatus
import com.techyourchance.unittestinginandroid.myexample.networking.NetworkErrorException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

private const val USERNAME = "username"
private const val PASSWORD = "password"
private const val AUTH_TOKEN = "authToken"
private const val NON_INITIALIZED_AUTH_TOKEN = "noAuthToken"

@RunWith(MockitoJUnitRunner::class)
class LoginUseCaseSyncTest {

	@Mock
	private lateinit var loginHttpEndpointSyncMock: LoginHttpEndpointSync

	@Mock
	private lateinit var authTokenCacheMock: AuthTokenCache

	@Mock
	private lateinit var eventBusPosterMock: EventBusPoster

	private lateinit var sut: LoginUseCaseSync

	@Before
	
	fun setup() {
		sut = LoginUseCaseSync(loginHttpEndpointSyncMock, authTokenCacheMock, eventBusPosterMock)
	}

	@Test
	fun loginSync_success_usernameAndPasswordPassedToEndpoint() {
		success()

		val ac = argumentCaptor<String>()

		sut.loginSync(USERNAME, PASSWORD)

		verify(loginHttpEndpointSyncMock).loginSync(ac.capture(), ac.capture())

		val id = ac.allValues[0]
		val pwd = ac.allValues[1]

		Assert.assertThat(id, `is`(USERNAME))
		Assert.assertThat(pwd, `is`(PASSWORD))
	}

	@Test
	fun loginSync_success_authTokenCached() {
		success()

		sut.loginSync(USERNAME, PASSWORD)

		verify(authTokenCacheMock).cacheAuthToken(anyString())
	}

	@Test
	fun loginSync_generalError_authTokenNotCached() {
		generalError()

		sut.loginSync(USERNAME, PASSWORD)

		verifyNoMoreInteractions(authTokenCacheMock)
	}

	@Test
	fun loginSync_authError_authTokenNotCached() {
		authError()

		sut.loginSync(USERNAME, PASSWORD)

		verifyNoMoreInteractions(authTokenCacheMock)
	}

	@Test
	fun loginSync_serverError_authTokenNotCached() {
		serverError()

		sut.loginSync(USERNAME, PASSWORD)

		verifyNoMoreInteractions(authTokenCacheMock)
	}

	@Test
	fun loginSync_success_loggedInEventPosted() {
		val ac = argumentCaptor<Any>()

		success()

		sut.loginSync(USERNAME, PASSWORD)

		verify(eventBusPosterMock).postEvent(ac.capture())

		val event = ac.allValues[0]

		assertThat(event, instanceOf(LoggedInEvent::class.java))
	}

	@Test
	fun loginSync_generalError_noInteractionWithEventBusPoster() {
		generalError()

		sut.loginSync(USERNAME, PASSWORD)

		verifyNoMoreInteractions(eventBusPosterMock)
	}

	@Test
	fun loginSync_authError_noInteractionWithEventBusPoster() {
		authError()

		sut.loginSync(USERNAME, PASSWORD)

		verifyNoMoreInteractions(eventBusPosterMock)
	}

	@Test
	fun loginSync_serverError_noInteractionWithEventBusPoster() {
		serverError()

		sut.loginSync(USERNAME, PASSWORD)

		verifyNoMoreInteractions(eventBusPosterMock)
	}

	@Test
	fun loginSync_success_successReturned() {
		success()

		val result = sut.loginSync(USERNAME, PASSWORD)

		assertThat(result, `is`(LoginUseCaseSync.UseCaseResult.SUCCESS))
	}

	@Test
	fun loginSync_serverError_failureReturned() {
		serverError()

		val result = sut.loginSync(USERNAME, PASSWORD)

		Assert.assertThat(result, `is`(LoginUseCaseSync.UseCaseResult.FAILURE))
	}

	@Test
	fun loginSync_authError_failureReturned() {
		authError()

		val result = sut.loginSync(USERNAME, PASSWORD)

		Assert.assertThat(result, `is`(LoginUseCaseSync.UseCaseResult.FAILURE))
	}

	@Test
	fun loginSync_generalError_failureReturned() {
		generalError()

		val result = sut.loginSync(USERNAME, PASSWORD)

		Assert.assertThat(result, `is`(LoginUseCaseSync.UseCaseResult.FAILURE))
	}

	@Test
	fun loginSync_networkError_networkErrorReturned() {
		networkError()

		val result = sut.loginSync(USERNAME, PASSWORD)

		Assert.assertThat(result, `is`(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR))
	}

	private fun success() {
		whenever(loginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
				.thenReturn(EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN))
	}

	private fun generalError() {
		whenever(loginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
				.thenReturn(EndpointResult(EndpointResultStatus.GENERAL_ERROR, NON_INITIALIZED_AUTH_TOKEN))
	}

	private fun authError() {
		whenever(loginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
				.thenReturn(EndpointResult(EndpointResultStatus.GENERAL_ERROR, NON_INITIALIZED_AUTH_TOKEN))
	}

	private fun serverError() {
		whenever(loginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
				.thenReturn(EndpointResult(EndpointResultStatus.SERVER_ERROR, NON_INITIALIZED_AUTH_TOKEN))
	}

	private fun networkError() {
		doThrow(NetworkErrorException())
				.whenever(loginHttpEndpointSyncMock).loginSync(anyString(), anyString())
	}
}
