package com.techyourchance.unittestinginandroid.myexample;

import com.techyourchance.unittestinginandroid.myexample.authtoken.AuthTokenCache;
import com.techyourchance.unittestinginandroid.myexample.eventbus.EventBusPoster;
import com.techyourchance.unittestinginandroid.myexample.eventbus.LoggedInEvent;
import com.techyourchance.unittestinginandroid.myexample.networking.LoginHttpEndpointSync;
import com.techyourchance.unittestinginandroid.myexample.networking.NetworkErrorException;

public class LoginUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final LoginHttpEndpointSync mLoginHttpEndpointSync;
    private final AuthTokenCache mAuthTokenCache;
    private final EventBusPoster mEventBusPoster;

    public LoginUseCaseSync(LoginHttpEndpointSync loginHttpEndpointSync,
                            AuthTokenCache authTokenCache,
                            EventBusPoster eventBusPoster) {
        mLoginHttpEndpointSync = loginHttpEndpointSync;
        mAuthTokenCache = authTokenCache;
        mEventBusPoster = eventBusPoster;
    }

    public UseCaseResult loginSync(String username, String password) {
        LoginHttpEndpointSync.EndpointResult endpointEndpointResult;
        try {
            endpointEndpointResult = mLoginHttpEndpointSync.loginSync(username, password);
        } catch (NetworkErrorException e) {
            return UseCaseResult.FAILURE;
        }

        mAuthTokenCache.cacheAuthToken(endpointEndpointResult.getAuthToken());
        if (isSuccessfulEndpointResult(endpointEndpointResult)) {
            mEventBusPoster.postEvent(new LoggedInEvent());
            return UseCaseResult.SUCCESS;
        } else {
            return UseCaseResult.FAILURE;
        }
    }

    private boolean isSuccessfulEndpointResult(LoginHttpEndpointSync.EndpointResult endpointResult) {
        return endpointResult.getStatus() == LoginHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
}
