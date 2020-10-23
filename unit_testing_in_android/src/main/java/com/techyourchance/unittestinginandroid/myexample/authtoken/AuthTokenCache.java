package com.techyourchance.unittestinginandroid.myexample.authtoken;

public interface AuthTokenCache {

    void cacheAuthToken(String authToken);

    String getAuthToken();
}
