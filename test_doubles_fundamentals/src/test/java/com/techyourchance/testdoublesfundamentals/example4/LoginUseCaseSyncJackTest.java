package com.techyourchance.testdoublesfundamentals.example4;

import com.techyourchance.testdoublesfundamentals.example4.LoginUseCaseSync.UseCaseResult;
import com.techyourchance.testdoublesfundamentals.example4.authtoken.AuthTokenCache;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.EventBusPoster;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.LoggedInEvent;
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginUseCaseSyncJackTest {

	public static final String USERNAME = "Username";
	public static final String PASSWORD = "Password";
	public static final String AUTH_TOKEN = "authToken";

	LoginHttpEndpointSyncTd mLoginHttpEndpointSyncTd;
	AuthTokenCacheTd mAuthTokenCacheTd;
	EventBusPosterTd mEventBusPosterTd;

	LoginUseCaseSync SUT;

	@Before
	public void setup() throws Exception {
		mLoginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd();
		mAuthTokenCacheTd = new AuthTokenCacheTd();
		mEventBusPosterTd = new EventBusPosterTd();
		SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd);
	}

	// User name 과 pwd 가 end point 에 전달된다.
	@Test
	public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mLoginHttpEndpointSyncTd.mUsername, is(USERNAME));
		assertThat(mLoginHttpEndpointSyncTd.mPassword, is(PASSWORD));
	}

	// 로그인 성공하면 - auth token 이 cache 된다.
	// 로그인 실패하면 - auth token 이 변하지 않는다.
	// 로그인 성공하면 - login event 가 EventBusPoster 로 post 된다.
	// 로그인 실패하면 - login event 가 EventBusPoster 에 post 되지 않는다.
	// 로그인 성공하면 - Success return 된다.
	// 로그인 실패하면 - Fail return 된다.
	// 네트워크 에러면 - Network error 가 return 된다.

	// ---------------------------------------------------------------------------------------------
	// Helper classes

	private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {

		public String mUsername;
		public String mPassword;

		@Override
		public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
			mUsername = username;
			mPassword = password;
			// FIXME : Jack
            return null;
			//return new LoginHttpEndpointSync.EndpointResult(EndpointResultStatus.GENERAL_ERROR, AUTH_TOKEN);
		}
	}

	private static class AuthTokenCacheTd implements AuthTokenCache {
		@Override
		public void cacheAuthToken(String authToken) {

		}

		@Override
		public String getAuthToken() {
			return null;
		}
	}

	private static class EventBusPosterTd implements EventBusPoster {
		@Override
		public void postEvent(Object event) {

		}
	}
}