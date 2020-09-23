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

	private static final String USERNAME = "Username";
	private static final String PASSWORD = "Password";
	private static final String AUTH_TOKEN = "authToken";
	private static final String NON_INITIALIZED_AUTH_TOKEN = "noAuthToken";

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
	@Test
	public void loginSync_success_authTokenCached() throws Exception {
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mAuthTokenCacheTd.getAuthToken(), is(AUTH_TOKEN));
	}

	// 로그인 실패하면 - auth token 이 변하지 않는다.
	// EndpointResultStatus 를 보니 여러 에러 상황이 있으니 이걸 참고로 예외 처리함
	@Test
	public void loginSync_generalError_authTokenNotCached() throws Exception {
		mLoginHttpEndpointSyncTd.mIsGeneralError = true;
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mAuthTokenCacheTd.getAuthToken(), is(NON_INITIALIZED_AUTH_TOKEN));
	}

	// 인증 에러인 경우
	@Test
	public void loginSync_authError_authTokenNotCached() throws Exception {
		mLoginHttpEndpointSyncTd.mIsAuthError = true;
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mAuthTokenCacheTd.getAuthToken(), is(NON_INITIALIZED_AUTH_TOKEN));
	}

	// 서버 에러인 경우
	@Test
	public void loginSync_serverError_authTokenNotCached() throws Exception {
		mLoginHttpEndpointSyncTd.mIsServerError = true;
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mAuthTokenCacheTd.getAuthToken(), is(NON_INITIALIZED_AUTH_TOKEN));
	}

	// 로그인 성공하면 - login event 가 EventBusPoster 로 post 된다.
	@Test
	public void loginSync_success_loggedInEventPosted() throws Exception {
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mEventBusPosterTd.mEvent, is(instanceOf(LoggedInEvent.class)));
	}

	// 로그인 실패하면 - login event 가 EventBusPoster 에 post 되지 않는다.
	@Test
	public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
		mLoginHttpEndpointSyncTd.mIsGeneralError = true;
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mEventBusPosterTd.mInteractionsCount, is(0));
	}

	@Test
	public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
		mLoginHttpEndpointSyncTd.mIsAuthError = true;
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mEventBusPosterTd.mInteractionsCount, is(0));
	}

	@Test
	public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
		mLoginHttpEndpointSyncTd.mIsServerError = true;
		SUT.loginSync(USERNAME, PASSWORD);
		assertThat(mEventBusPosterTd.mInteractionsCount, is(0));
	}

	// 로그인 성공하면 - Success return 된다.
	@Test
	public void loginSync_success_successReturned() throws Exception {
		UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
		assertThat(result, is(UseCaseResult.SUCCESS));
	}

	// 로그인 실패하면 - Fail return 된다.
	@Test
	public void loginSync_serverError_failureReturned() throws Exception {
		mLoginHttpEndpointSyncTd.mIsServerError = true;
		UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
		assertThat(result, is(UseCaseResult.FAILURE));
	}

	@Test
	public void loginSync_authError_failureReturned() throws Exception {
		mLoginHttpEndpointSyncTd.mIsAuthError = true;
		UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
		assertThat(result, is(UseCaseResult.FAILURE));
	}

	@Test
	public void loginSync_generalError_failureReturned() throws Exception {
		mLoginHttpEndpointSyncTd.mIsGeneralError = true;
		UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
		assertThat(result, is(UseCaseResult.FAILURE));
	}

	// 네트워크 에러면 - Network error 가 return 된다.
	@Test
	public void loginSync_networkError_networkErrorReturned() throws Exception {
		mLoginHttpEndpointSyncTd.mIsNetworkError = true;
		UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
		assertThat(result, is(UseCaseResult.NETWORK_ERROR));
	}

	// ---------------------------------------------------------------------------------------------
	// Helper classes

	private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {

		public String mUsername;
		public String mPassword;
		public boolean mIsGeneralError;
		private boolean mIsAuthError;
		private boolean mIsServerError;
		private boolean mIsNetworkError;

		@Override
		public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
			mUsername = username;
			mPassword = password;
			if (mIsGeneralError) {
				return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");
			} else if (mIsAuthError) {
				return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");
			}  else if (mIsServerError) {
				return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
			} else if (mIsNetworkError) {
				throw new NetworkErrorException();
			} else {
				return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
			}
		}
	}

	private static class AuthTokenCacheTd implements AuthTokenCache {

		private String mAuthToken = NON_INITIALIZED_AUTH_TOKEN;

		@Override
		public void cacheAuthToken(String authToken) {
			mAuthToken = authToken;
		}

		@Override
		public String getAuthToken() {
			return mAuthToken;
		}
	}

	private static class EventBusPosterTd implements EventBusPoster {

		public Object mEvent;
		public int mInteractionsCount;

		@Override
		public void postEvent(Object event) {
			mEvent = event;
		}
	}
}