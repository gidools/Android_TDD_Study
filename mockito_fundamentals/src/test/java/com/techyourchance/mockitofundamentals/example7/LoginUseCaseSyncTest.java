package com.techyourchance.mockitofundamentals.example7;

import com.techyourchance.mockitofundamentals.example7.authtoken.AuthTokenCache;
import com.techyourchance.mockitofundamentals.example7.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync.EndpointResult;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync.EndpointResultStatus;
import com.techyourchance.mockitofundamentals.example7.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginUseCaseSyncTest {

    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String AUTH_TOKEN = "authToken";
    private static final String NON_INITIALIZED_AUTH_TOKEN = "noAuthToken";

    // FIXME Change test double to mock
    LoginHttpEndpointSync mLoginHttpEndpointSyncMock;
    AuthTokenCache mAuthTokenCacheMock;
    EventBusPoster mEventBusPosterMock;

    LoginUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        mLoginHttpEndpointSyncMock = mock(LoginHttpEndpointSync.class);
        mAuthTokenCacheMock = mock(AuthTokenCache.class);
        mEventBusPosterMock = mock(EventBusPoster.class);
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncMock, mAuthTokenCacheMock, mEventBusPosterMock);

        // given
        mockLoginSuccess();
    }

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);

        // when
        SUT.loginSync(USERNAME, PASSWORD);

        // then
        verify(mLoginHttpEndpointSyncMock, times(1)).
                loginSync(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();

        // then
        assertThat(captures.get(0), is(USERNAME));
        assertThat(captures.get(1), is(PASSWORD));
    }

    @Test
    public void loginSync_success_authTokenCached() throws Exception {
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mAuthTokenCacheMock.getAuthToken(), is(AUTH_TOKEN));
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsGeneralError = true;
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mAuthTokenCacheMock.getAuthToken(), is(NON_INITIALIZED_AUTH_TOKEN));
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsAuthError = true;
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mAuthTokenCacheMock.getAuthToken(), is(NON_INITIALIZED_AUTH_TOKEN));
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsServerError = true;
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mAuthTokenCacheMock.getAuthToken(), is(NON_INITIALIZED_AUTH_TOKEN));
    }

    @Test
    public void loginSync_success_loggedInEventPosted() throws Exception {
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mEventBusPosterMock.mEvent, is(instanceOf(LoggedInEvent.class)));
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsGeneralError = true;
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mEventBusPosterMock.mInteractionsCount, is(0));
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsAuthError = true;
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mEventBusPosterMock.mInteractionsCount, is(0));
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsServerError = true;
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mEventBusPosterMock.mInteractionsCount, is(0));
    }

    @Test
    public void loginSync_success_successReturned() throws Exception {
//        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void loginSync_serverError_failureReturned() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsServerError = true;
//        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_authError_failureReturned() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsAuthError = true;
//        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_generalError_failureReturned() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsGeneralError = true;
//        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_networkError_networkErrorReturned() throws Exception {
//        mLoginHttpEndpointSyncMock.mIsNetworkError = true;
//        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    private void mockLoginSuccess() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN));
    }
}