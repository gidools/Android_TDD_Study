package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateUsernameUseCaseSyncTest {

	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	private UpdateUsernameUseCaseSync SUT;
	private UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
	private UsersCache usersCacheMock;
	private EventBusPoster eventBusPosterMock;

	@Before
	public void setup() throws NetworkErrorException {
		updateUsernameHttpEndpointSyncMock = Mockito.mock(UpdateUsernameHttpEndpointSync.class);
		usersCacheMock = Mockito.mock(UsersCache.class);
		eventBusPosterMock = Mockito.mock(EventBusPoster.class);

		SUT = new UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSyncMock,
				usersCacheMock, eventBusPosterMock);

		success();
	}

	// user id and name passed to the endpoint
	@Test
	public void updateUserName_success_userIdAndPwdPassedToEndPoint() throws Exception {
		// Arrange
		ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
		// Act
		SUT.updateUsernameSync(USER_ID, USER_NAME);
		verify(updateUsernameHttpEndpointSyncMock, times(1))
				.updateUsername(ac.capture(), ac.capture());
		// Assert
		List<String> captures = ac.getAllValues();
		assertThat(captures.get(0), is(USER_ID));
		assertThat(captures.get(1), is(USER_NAME));
	}

	// if update succeed user cached
	@Test
	public void updateUserName_success_userCached() throws Exception {
		// Arrange
		// Act
		SUT.updateUsernameSync(USER_ID, USER_NAME);
		// Assert
		verify(usersCacheMock, times(1)).cacheUser(ArgumentMatchers.<User>any());
	}

	// if general error user not cached

	@Test
	public void updateUserName_generalError_userNotCached() throws Exception {
		// Arrange
		generalError();
		// Act
		SUT.updateUsernameSync(USER_ID, USER_NAME);
		// Assert
		verify(usersCacheMock, never()).cacheUser(any(User.class));
	}

	private void generalError() throws NetworkErrorException {
		when(updateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
				.thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR, anyString(), anyString()));
	}

	// if auth error user not cached
	// if server error user not cached
	// if success success returned
	// if server error failure returned
	// if auth error failure returned
	// if general error failure returned
	// if network error network error returned

	private void success() throws NetworkErrorException {
		when(updateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
				.thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USER_NAME));
	}
}