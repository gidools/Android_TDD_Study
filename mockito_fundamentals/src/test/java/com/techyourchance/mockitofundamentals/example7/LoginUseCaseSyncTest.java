package com.techyourchance.mockitofundamentals.example7;

import com.techyourchance.mockitofundamentals.example7.LoginUseCaseSync.UseCaseResult;
import com.techyourchance.mockitofundamentals.example7.authtoken.AuthTokenCache;
import com.techyourchance.mockitofundamentals.example7.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        SUT = new LoginUseCaseSync();
    }

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
//        SUT.loginSync(USERNAME, PASSWORD);
//        assertThat(mLoginHttpEndpointSyncMock.mUsername, is(USERNAME));
//        assertThat(mLoginHttpEndpointSyncMock.mPassword, is(PASSWORD));
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
}